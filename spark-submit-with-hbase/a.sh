#!/bin/bash

source ~/.profile

mvn clean package -U -Paliyun -Dmaven.test.skip=true

scp target/*.jar root@192.168.3.97:/data/

#spark-submit --class cn.eastseven.App --master local target/spark-submit-with-hbase-1.0-SNAPSHOT.jar
#spark-submit target/spark-submit-with-hbase-1.0-SNAPSHOT.jar

#ssh root@192.168.3.97 "cd /data && sh a.sh"