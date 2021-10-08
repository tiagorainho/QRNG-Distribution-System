#!/usr/bin/bash

function destroy_docker_container {
    sudo docker-compose -f ../$1/docker-compose.yml down
}

destroy_docker_container "QrngService"
destroy_docker_container "PrimeService"
destroy_docker_container "UserService"