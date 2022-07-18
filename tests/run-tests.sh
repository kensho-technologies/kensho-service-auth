cd $(git rev-parse --show-toplevel)

docker build -f tests/docker/java.Dockerfile . -t kensho-auth-java --build-arg PRIVATE_KEY_FILE="${PRIVATE_KEY_FILE}" --build-arg SCOPES="${SCOPES}" --build-arg CLIENT_ID="${CLIENT_ID}"
docker build -f tests/docker/python.Dockerfile . -t kensho-auth-python --build-arg PRIVATE_KEY_FILE="${PRIVATE_KEY_FILE}" --build-arg SCOPES="${SCOPES}" --build-arg CLIENT_ID="${CLIENT_ID}"
echo "Java Test"
docker run kensho-auth-java
echo "Python Test"
docker run kensho-auth-python
