docker build . -t kensho-auth-js
docker run -v ${PRIVATE_KEY_FILE}:/home/app/src/private_key.pem -e PRIVATE_KEY_FILE="/home/app/src/private_key.pem" -e SCOPES=${SCOPES} -e CLIENT_ID=${CLIENT_ID} kensho-auth-js
