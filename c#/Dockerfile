FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
COPY ./ /home/app/src/
WORKDIR /home/app/src/auth-tests
CMD dotnet test
