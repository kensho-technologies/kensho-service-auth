FROM python:3.9
COPY ./ /home/app/src/
RUN pip install pyjwt requests cryptography
WORKDIR /home/app/src/
CMD python -m unittest test_auth.py
