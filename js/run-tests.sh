#!/bin/bash
docker build $(dirname $0) -t kensho-auth-js
docker run -e PRIVATE_KEY_FILE="/home/app/src/private_key.pem" -e SCOPES -e CLIENT_ID --rm kensho-auth-js
