#!/usr/bin/bash

function destroy_docker_container {
    docker-compose -f ../$1/docker-compose.yml down
}

destroy_docker_container "DiscoveryService"
destroy_docker_container "QrngService"
destroy_docker_container "PrimeService"
destroy_docker_container "UserService"
destroy_docker_container "APIGateway"
destroy_docker_container "QRNGMachine"
docker network rm eureka_bridge