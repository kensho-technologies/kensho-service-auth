const OKTA_TOKEN_ENDPOINT = "https://kensho.okta.com/oauth2/default/v1/token";
class KenshoAuth {
    constructor(client_id, private_key_file, kid) {
        this.client_id = client_id;
        this.private_key_file = private_key_file;
        this.kid = kid;
    }

    async make_jwt() {
        const fs = require('fs');
        const crypto = require('crypto');
        const privatePem = fs.readFileSync(this.private_key_file);
        const privateKey = crypto.createPrivateKey({
            key: privatePem,
        });
        const jose = require('jose')
        
        let iat = Date.now() / 1000;
        const jwt = await new jose.SignJWT({})
            .setProtectedHeader({
                typ: 'JWT',
                alg: 'RS256',
                kid: this.kid,
            })
            .setAudience(OKTA_TOKEN_ENDPOINT)
            .setIssuedAt(iat)
            .setExpirationTime(iat+ (5 * 60))
            .setSubject(this.client_id)
            .setIssuer(this.client_id)
            .sign(privateKey)
        return jwt;
    }

    async get_access_token(scopes) {
        let client_assertion = await this.make_jwt();
        let data = {
            "client_assertion": client_assertion,
            "client_assertion_type": "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
            "scope": scopes,
            "grant_type": "client_credentials",
        }
        var formBody = [];
        for (var property in data) {
          var encodedKey = encodeURIComponent(property);
          var encodedValue = encodeURIComponent(data[property]);
          formBody.push(encodedKey + "=" + encodedValue);
        }
        formBody = formBody.join("&");
        
        const response = await fetch(OKTA_TOKEN_ENDPOINT, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: formBody
          });
        let json = await response.json();
        return json["access_token"];
    }
}

let auth = new KenshoAuth(process.argv[2], process.argv[3], process.argv[4]);
auth.get_access_token(process.argv.slice(5).join(" ")).then(token => {
    console.log(token);
})
module.exports = {
    KenshoAuth: KenshoAuth
}
