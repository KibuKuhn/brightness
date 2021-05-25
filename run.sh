#!/bin/bash
dir=$(dirname $(readlink -f "$0"))
cd $dir
args=
for arg in "$@"
do
    args="$args $arg"
done

#java -Ddelay=5000 -jar brightness-2.0.0.jar &
java -jar brightness-2.0.0.jar &
