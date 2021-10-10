from types import MethodType
from flask import Flask, Response, request
import random, json, requests, time

app = Flask(__name__)

isRegistered = False
api_key = 'key'
qrng_machine_name = "Machine QRNG"
machine_type = 'QUANTUM'
endpoint = 'http://192.168.1.111:5000'

@app.route('/random', methods=['GET'])
def get_random():
    bytes = request.args.get('bytes', default=1, type=int)
    n = request.args.get('n', default=1, type=int)
    random_bits = [int(random.getrandbits(bytes*8)) for _ in range(0, n)]
    return Response(
        json.dumps({'numbers': random_bits }),
        status=200,
        mimetype="application/json"
    )

def registry():
    global isRegistered, api_key, qrng_machine_name, machine_type, endpoint
    QRNG_endpoint = 'http://192.168.1.111:8001'
    while(not isRegistered):
        try:
            response = requests.post(
                url = QRNG_endpoint + '/api/generator',
                headers = {'content-type':'application/json', 'Authorization': api_key},
                json = {
                    'name': qrng_machine_name,
                    'type': machine_type,
                    'url': endpoint
                },
                timeout=2
            )
            print(response.status_code)
            isRegistered = int(response.status_code / 100) == 2
            if isRegistered:
                print("Registried with success")
                return
        except Exception:
            print("Error at the registry with " + QRNG_endpoint)
        if not isRegistered: time.sleep(3)
    


if __name__ == '__main__':
    registry()
    app.run(debug=True, host='0.0.0.0', port=5000)