#!/bin/sh

if [ -z $1 ]; then
    echo "Must specify port"
    exit 1
fi

if [ -z $2 ]; then
    echo "Must specify context directory"
    exit 1
fi

if [ -z $3 ]; then
  SYSPROP=""
else
  SYSPROP="-Dsysprop-resource=$3"
fi

java $JPDA $SYSPROP -jar launcher.jar StartJetty --addclasspath config --addjardir jetty $1 war $2
