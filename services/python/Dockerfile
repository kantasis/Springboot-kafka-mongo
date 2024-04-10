FROM python:3.11

# Don't use the 3.12 version. It's new and doesn't work well with kafka.
# FROM python:3.12

RUN apt update
RUN pip install kafka-python
RUN apt install netcat-traditional


CMD ["tail", "-f", "/dev/null"]