#!/bin/bash

docker-compose -f redis-cluster.yml down
docker-compose -f redis-cluster.yml up -d --scale sentinel=3 --scale slave=2

docker ps