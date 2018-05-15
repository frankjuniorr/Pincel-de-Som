#!/bin/bash

# ##############################################################################
# Description:
#   Script to zip all PureData files in android path and update android code.
#    Run this script always that puredata code is modified
# ##############################################################################

# create local directory 'pincel_de_som'
mkdir pincel_de_som

# copy all puredata files to new directory
cp -r ../audio/* pincel_de_som/

# zip the directory
zip -r pincel_de_som.zip pincel_de_som

# delete directory
rm -rf pincel_de_som/

# move zip file to android path
mv pincel_de_som.zip ../app/app/src/main/res/raw
