#!/bin/bash

source ~/.profile

build_dir=`pwd`

cd ..
mvn clean package -Dmaven.test.skip=true -Paliyun -U
cp target/*.jar $build_dir/app.jar

cd $build_dir
docker build -t eastseven/redis-sentinel-with-spring .