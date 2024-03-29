import json

import requests


def main():
    params = {"station_id": 1}
    body = {"station_name": "g4t1", "description": "peakaboo", "temperature_data": [.1, .2, .3]}
    rc = requests.post('http://127.0.0.1:8000/temperature/', params=params, data=json.dumps(body))
    print(rc.json())


if __name__ == '__main__':
    main()
