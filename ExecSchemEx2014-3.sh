d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-35.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-36.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-37.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-38.conf"
