
## Setup

1. Create a virtual environment

```
$ python -m venv .venv
$ . .venv/bin/activate
```

2. Install dependencies

```
$ pip install pyjwt requests cryptography
```

3. Run the script

```
$ python kensho_auth.py <clientid> <privatekeyfilename> <scope>
```

