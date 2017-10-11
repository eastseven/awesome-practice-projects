#!/bin/bash

var_app=redis-sentinel-with-spring-app

docker stop $var_app
docker rm $var_app
docker run -d --name $var_app \
 --network=redis_default \
 --link redis_sentinel_1:redis_sentinel_1 \
 -e SPRING_PROFILES_ACTIVE=docker \
 eastseven/redis-sentinel-with-spring

docker logs -f $var_app