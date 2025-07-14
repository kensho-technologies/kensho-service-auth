FROM python:3.9
WORKDIR /home/app/src/
COPY . .
RUN pip install -r requirements.txt
CMD python -m unittest test_auth.py
