import json

import requests


def main():
    station_id = 1
    body = {"station_name": "g4t1", "description": "peakaboo", "temperature_data": [.1, .2, .3]}
    rc = requests.post(f'http://127.0.0.1:8000/{station_id}/temperature', data=json.dumps(body))
    print(rc.json())


if __name__ == '__main__':
    main()
