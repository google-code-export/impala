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

if [ -z $4 ]; then
  CONFIGDIR=""
else
  CONFIGDIR="$4"
fi

if [ -z $5 ]; then
  SCHEME="http"
else
  SCHEME="$5"
fi

java $JPDA $SYSPROP -jar launcher.jar RunJetty --addclasspath $CONFIGDIR --addjardir jetty $1 war $2 $SCHEME