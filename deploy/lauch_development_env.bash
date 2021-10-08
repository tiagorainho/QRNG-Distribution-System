#!/usr/bin/bash

function start_service {
    konsole --hold --new-tab -e sh -c 'cd ../'$1' && ./mvnw -e spring-boot:run'
    echo -e "\e[1;32m "$1" Application started. \e[0m"
}

function deploy_docker_container {
    sudo docker-compose -f ../$1/docker-compose.yml down
    sudo docker-compose -f ../$1/docker-compose.yml up -d
    echo -e "\e[1;32m "$1" Docker Container started. \e[0m"
}



deploy_docker_container "QrngService"
deploy_docker_container "PrimeService"
deploy_docker_container "UserService"


start_service "DiscoveryService" &
sleep 3
start_service "QrngService" &
start_service "PrimeService" &
start_service "APIGateway" &
start_service "UserService" &

sleep 3

sudo docker-compose ../QRNGMachine/docker-compose up --build