FROM python:3.9
WORKDIR /home/app/src/
COPY . .
RUN pip install -r requirements.txt
RUN openssl genrsa -out private_key.pem 2048
CMD python -m unittest test_auth.py
