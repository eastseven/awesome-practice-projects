#!/bin/bash

mvn clean package -U -Paliyun -Dmaven.test.skip=true
scp target/*.jar eastseven@192.168.31.30:/home/eastseven/dev/workspace/book-crawler/app.jar