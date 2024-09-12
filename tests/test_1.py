#!/bin/env python3

import requests
import json


def test_get():

   url='http://localhost:8180'
   endpoint_str='api/data/data'
   payload = {}
   headers = {
   'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnZW9yZ2UiLCJpYXQiOjE3MjYxMjQ2NTgsImV4cCI6MTcyNjIxMTA1OH0.4UAuNxm7bJk93Fx9KO51es1wWXZMp9dXke2xp1pIBZ8'
   }

   response = requests.request(
      "GET",
      f"{url}/{endpoint_str}",
      headers=headers, 
      data=payload,
   )

   # binary response
   # print(response.content)

   # Text response
   print(response.text)

   # JSON pretty printed
   # print(json.dumps(
   #    json.loads(response.text),
   #    indent=3
   # ))
