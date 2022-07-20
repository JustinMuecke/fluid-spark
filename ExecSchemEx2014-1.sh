d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi


sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-24.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-25.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-26.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-27.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-28.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-29.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-30.conf"
