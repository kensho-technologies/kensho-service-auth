FROM python:3.9
ARG PRIVATE_KEY_FILE
ARG CLIENT_ID
ARG SCOPES
COPY ${PRIVATE_KEY_FILE} /home/app/src/private_key.pem
ENV PRIVATE_KEY_FILE=/home/app/src/private_key.pem \
    SCOPES=${SCOPES} \ 
    CLIENT_ID=${CLIENT_ID}
COPY python /home/app/src/python
COPY tests /home/app/src/tests
RUN pip install pyjwt requests cryptography
WORKDIR /home/app/src/
CMD python -m unittest tests/test_auth.py 