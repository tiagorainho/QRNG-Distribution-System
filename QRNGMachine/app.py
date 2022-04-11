from types import MethodType
from flask import Flask, Response, request
import random, json, requests, time

app = Flask(__name__)

api_key = 'key'
qrng_machine_name = "Machine QRNG"
machine_type = 'QUANTUM'
endpoint = 'http://192.168.46.104:5001'
service_endpoint = 'http://192.168.46.104:8001'

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

def registry(qrng_machine_name, endpoint, machine_type, api_key, QRNG_service_endpoint):
    isRegistered = False
    while(not isRegistered):
        response = requests.post(
            url = QRNG_service_endpoint + '/api/generator',
            headers = {'content-type':'application/json', 'Authorization': api_key},
            json = {
                'name': qrng_machine_name,
                'type': machine_type,
                'url': endpoint
            },
            timeout=2
        )
        isRegistered = int(response.status_code / 100) == 2
        if isRegistered:
            print(str(response.status_code) + ": Registried '" + qrng_machine_name + "' with success")
            return
        else:
            print(str(response.status_code) + ": Error at the registry with " + QRNG_service_endpoint)
            time.sleep(3)

if __name__ == '__main__':
    registry(qrng_machine_name, endpoint, machine_type, api_key, service_endpoint)
    app.run(debug=False, host='0.0.0.0', port=5000)