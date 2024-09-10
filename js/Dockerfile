FROM node:18
WORKDIR /home/app/src/
COPY . .
RUN openssl genrsa -out private_key.pem 2048
RUN npm install
CMD npm test
