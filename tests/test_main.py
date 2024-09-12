#!/bin/env python3

# This one is alternative but it works
# !/bin/env -S python3 -m unittest

import unittest
import requests

class TestAuth(unittest.TestCase):

   apiHost_str='localhost'
   apiPort_str='8180'
   api_url=f'http://{apiHost_str}:{apiPort_str}'

   @classmethod
   def setUpClass(cls):
      """this one runs at the very start of all"""

      # headers_dict = {
      #    'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnZW9yZ2UiLCJpYXQiOjE3MjYxMjQ2NTgsImV4cCI6MTcyNjIxMTA1OH0.4UAuNxm7bJk93Fx9KO51es1wWXZMp9dXke2xp1pIBZ8'
      # }

      print("Ran setup class")

   # I don't know why the @classmethod is necessary
   @classmethod
   def tearDownClass(cls):
      """this one runs at the very end"""
      pass

   def setUp(self):
      """This one runs before every single test"""
      print("Ran setup test")
      pass
   def tearDown(self):
      """this one runs after single test"""
      pass

   def test_login(self):
      print("running test")
      endpoint_str = "api/auth/signin"
      payload_dict = {
         "username": "george",
         "password": "1234567"
      }
      headers_dict = {
         'Content-Type': 'application/json'
      }

      response_dict = requests.request(
         "POST",
         f"{self.api_url}/{endpoint_str}",
         headers=headers_dict,
         json=payload_dict,
      )

      self.assertEqual(response_dict.status_code,200)

if __name__=='__main__':
   unittest.main()

