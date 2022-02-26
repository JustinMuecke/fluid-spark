#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

# convenience wrapper: $1:=config, $2:=resume point (first snapshot), $3:=end point (last snapshot)
# sbt "runMain Main $1 $2 $3"
export SBT_OPTS"-Xmx600g -Xms600g"
for config in resources/configs/DOGS_exp/
do
 echo "Running $config"
 sbt "runMain Main resources/configs/DOGS_exp/$config"
done
