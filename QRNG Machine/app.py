from types import MethodType
from flask import Flask, Response, request
import random, json
import requests

app = Flask(__name__)

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
    endpoint = 'http://localhost:8000'
    requests.post(
        url = endpoint + '/api/generator',
        headers = {'content-type':'application/json', 'Authorization': 'key'},
        json = {
            'name': 'QRNG2',
            'type': 'QUANTUM',
            'url': 'http://192.168.1.188:5000'
        }
    )


if __name__ == '__main__':
    registry()
    app.run(debug=True, host='0.0.0.0')