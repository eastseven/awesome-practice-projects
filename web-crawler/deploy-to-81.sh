#!/bin/bash

mvn clean package -Dmaven.test.skip=true -Phar
scp target/*.jar root@192.168.3.81:/data/