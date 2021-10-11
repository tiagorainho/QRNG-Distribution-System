#!/usr/bin/bash

function deploy_docker_container {
    sudo docker-compose -f ../$1/docker-compose.yml down
    sudo docker-compose -f ../$1/docker-compose.yml up -d
    echo -e "\e[1;32m "$1" Docker Container started. \e[0m"
}

function deploy_spring_boot_service {
    konsole --hold --new-tab -e sh -c 'cd ../'$1' && docker-compose down && docker-compose up'
}

# configure bridge
docker network create eureka_bridge

# deploy services
deploy_spring_boot_service "DiscoveryService" &
sleep 3 # head start in order for the rest of the services to not fail in the beggining
deploy_spring_boot_service "APIGateway" &
deploy_spring_boot_service "UserService" &
deploy_spring_boot_service "QrngService" &
deploy_spring_boot_service "PrimeService" &
deploy_spring_boot_service "QRNGMachine" &
