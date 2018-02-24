#!/bin/bash
if [ "$#" -ne "1" ]
then
    echo "argument env ('local' or 'poc' or 'dev') is required, usage: $0 <env>"
else
    export ENV=$1
    echo "running with env '$ENV'..."
    /usr/local/openresty/nginx/sbin/nginx -p `pwd` -c conf/$ENV.conf
fi
