# QRNG-Distribution-System
Distribution system for real random numbers and prime numbers based on the real random numbers

# Motivation
There are already Quantum Random Number Generators, this project was developed in order to abstract multiple QRNGs in a single API.

# How to use

## Instalation

First we need to install Docker on your system.
Ubuntu:
```
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
```

Setup the bridge network for the Eureka Service
```
docker network create eureka_bridge
```

Now we can already use the building tools inside this project.

## Usage

### Running Services Individually

Each Service lives inside a container, in order to use the service we need to use the container.

Build the container
```
docker-compose build
```

Launch the container
```
docker-compose up
```

Shut the container down
```
docker-compose down
```


### Running All Services at once

How to build the containers:
```
bash build_containers.bash
```

How to run the containers
```
bash launch_containers.bash
```

### Destroy the Containers from the System
```
bash destroy_containers.bash
```
