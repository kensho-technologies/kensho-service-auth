import { KenshoAuth } from "../kensho-auth.js";
const chai = require('chai')
const expect = chai.expect
describe('KenshoAuth', () => {
    it('should get access token', () => {
        let client_id = process.env.CLIENT_ID;
        let private_key_file = process.env.PRIVATE_KEY_FILE;
        let scopes = process.env.SCOPES;
        let auth = new KenshoAuth(client_id, private_key_file);
        return auth.get_access_token(scopes).then(token => {
            expect(token).match(/^.{90}\..{300,}\..{342}$/)
        })
    })
})
