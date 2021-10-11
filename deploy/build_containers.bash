#!/usr/bin/bash

function build_docker_container {
    konsole --hold --new-tab -e sh -c '
        cd ../'$1' &&
        docker-compose build &&
        echo -e "\e[1;32m "'$1'" Docker Container created. \e[0m"
    '
}

build_docker_container "DiscoveryService" &
build_docker_container "APIGateway" &
build_docker_container "UserService" &
build_docker_container "QrngService" &
build_docker_container "PrimeService" &
build_docker_container "QRNGMachine" &