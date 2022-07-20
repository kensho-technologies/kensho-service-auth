using System.Security.Cryptography;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json.Linq;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.OpenSsl;
using Org.BouncyCastle.Security;

class Auth
{
    const string OKTA_TOKEN_ENDPOINT = "https://kensho.okta.com/oauth2/default/v1/token";
    string client_id, private_key_file;

    public Auth(string client_id, string private_key_file)
    {
        this.client_id = client_id;
        this.private_key_file = private_key_file;
    }

    public string makeJWT()
    {
        string private_key_pem = File.ReadAllText(this.private_key_file);

        RSAParameters rsaParams;
        using (var tr = new StringReader(private_key_pem))
        {
            var pemReader = new PemReader(tr);
            var keyPair = pemReader.ReadObject() as AsymmetricCipherKeyPair;
            if (keyPair == null)
            {
                throw new Exception("Could not read RSA private key");
            }
            var privateRsaParams = keyPair.Private as RsaPrivateCrtKeyParameters;
            rsaParams = DotNetUtilities.ToRSAParameters(privateRsaParams);
        }

        using (RSACryptoServiceProvider rsa = new RSACryptoServiceProvider())
        {
            rsa.ImportParameters(rsaParams);

            var current_time = EpochTime.GetIntDate(DateTime.Now);

            var payload = new Dictionary<string, object>()
            {
                { "aud", OKTA_TOKEN_ENDPOINT },
                { "iat", current_time},
                { "exp",  current_time + (5*60)},
                { "sub", this.client_id },
                { "iss", this.client_id }
            };
            return Jose.JWT.Encode(payload, rsa, Jose.JwsAlgorithm.RS256);
        }
    }

    public async Task<string> getAccessTokenAsync(string scopes)
    {
        HttpClient client = new HttpClient();
        string client_assertion = makeJWT();
        var values = new Dictionary<string, string>
        {
            { "client_assertion", client_assertion },
            { "client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer" },
            { "scope", scopes },
            { "grant_type", "client_credentials" }
        };

        var content = new FormUrlEncodedContent(values);
        var response = await client.PostAsync(OKTA_TOKEN_ENDPOINT, content);
        response.EnsureSuccessStatusCode();
        string response_body = await response.Content.ReadAsStringAsync();
        JObject json = JObject.Parse(response_body);
        return JObject.Parse(response_body)["access_token"]!.ToString();
    }
}



class App
{
    static async Task Main(string[] args)
    {
        Auth auth = new(args[0], args[1]);
        Console.WriteLine(await auth.getAccessTokenAsync(String.Join(" ", args[2..])));
    }
}
