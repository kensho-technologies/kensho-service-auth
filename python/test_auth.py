import os
import subprocess
import re
import unittest

PRIVATE_KEY_FILE=os.getenv("PRIVATE_KEY_FILE")
CLIENT_ID=os.getenv("CLIENT_ID")
SCOPES=os.getenv("SCOPES")


class TestAuth(unittest.TestCase):
    """Testing auth methods"""


    def test_python(self):
        output = subprocess.check_output(f"python kensho_auth.py {CLIENT_ID} {PRIVATE_KEY_FILE} {SCOPES}", shell=True).decode('utf-8')
        is_token=bool(re.match("^.{90}\..{300,}\..{342}$", output))
        assert is_token == True, "Error running python auth"
