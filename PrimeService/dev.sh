#!/bin/sh

sudo docker-compose -f docker-compose.yml down
sudo docker-compose -f docker-compose.yml up -d