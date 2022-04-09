FROM python:3.8

WORKDIR /usr/src/app
COPY . .

RUN pip install --upgrade pip
RUN pip install -r requirements.txt


# -u is used for buffering reasons in order to work with time.sleep on python
CMD ["python3", "-u", "app.py"]
