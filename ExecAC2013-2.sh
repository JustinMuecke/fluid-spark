d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi


sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-20.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-23.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-24.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-25.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-26.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-27.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-28.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-29.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-30.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-33.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-35.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-36.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-37.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-38.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-39.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-42.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-46.conf"
sbt "runMain Main resources/configs/DOGS_exp/AC/2013/AC-2013-50.conf"

