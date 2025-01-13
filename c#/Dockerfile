FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
WORKDIR /home/app/src/auth-tests
COPY . .
RUN openssl genrsa -out private_key.pem 2048
CMD dotnet test
