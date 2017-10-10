#!/bin/bash

source ~/.profile

mvn clean package -Dmaven.test.skip=true -Paliyun

java -Xmx512m -Xms512m -jar target/web-crawler-0.0.1-SNAPSHOT.jar start