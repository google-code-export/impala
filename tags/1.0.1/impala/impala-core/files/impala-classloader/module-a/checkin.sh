#!/bin/sh

########################################################################################
# Shell script to check in from bin directories: can't seem to get it to work in eclipse
########################################################################################

names=( a b c d e f g )

for item in ${names[@]}
do
  echo $item
  cd ../module-$item
  svn add bin/*.class 
  svn commit -m "Ticket #21: adding bin directories"  bin
done

