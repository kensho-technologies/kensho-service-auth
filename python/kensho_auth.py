import sys
import time
from typing import Any, Dict

import requests
import jwt


class KenshoAuth:
    OKTA_TOKEN_ENDPOINT = "https://kensho.okta.com/oauth2/default/v1/token"

    def __init__(self, client_id: str, private_key_filename: str):
        self.client_id = client_id
        self.private_key_filename = private_key_filename

    def make_self_claims(self):
        """Make a dictionary of claims about outselves."""
        iat = int(time.time())  # Created at the current time
        claims = {
            "aud": self.OKTA_TOKEN_ENDPOINT,
            "iat": iat,
            "exp": iat + (5 * 60),  # Expires in 5 minutes
            "sub": self.client_id,
            "iss": self.client_id,
        }
        return claims

    def make_jwt(self, claims: Dict[str,Any]):
        """Sign the claims and form a Json Web Token."""
        with open(self.private_key_filename, "rb") as f:
            private_key = f.read()
        return jwt.encode(claims, private_key, algorithm="RS256")

    def get_access_token(self, scopes: [str]):
        """Obtain an access token from Okta for given scopes."""
        claims = self.make_self_claims()
        client_assertion = self.make_jwt(claims)
        data = {
            "client_assertion": client_assertion,
            "client_assertion_type": "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
            "scope": " ".join(scopes),
            "grant_type": "client_credentials",
        }
        r = requests.post(self.OKTA_TOKEN_ENDPOINT, data=data)
        if r.status_code != 200:
            raise RuntimeError("Okta error: [%s]" % r.text)
        data = r.json()
        access_token = data["access_token"]
        return access_token


def main(args: [str]):
    """Obtain and print an access token."""
    if len(args) <= 2:
        print("Usage: client_id private_key_filename scopenames...")
        sys.exit(1)

    auth = KenshoAuth(args[0], args[1])
    access_token = auth.get_access_token(args[2:])
    print(access_token)


if __name__ == "__main__":
    main(sys.argv[1:])
