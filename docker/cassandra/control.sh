#!/bin/bash

BUILD_TAG=myinvestor-cassandra
RUN_NAME=cassandra


build() {
    echo
    echo "==== Build ===="

    sudo docker build -t $BUILD_TAG .   

    echo   
}

start() {

    echo
    echo "==== start ===="

    sudo docker run --name $RUN_NAME -d $BUILD_TAG   

    echo    
}

stop() {

    echo
    echo "==== stop ===="

    sudo docker stop $RUN_NAME   

    echo    
}


case "$1" in
    'build')
            build
            ;;
    'start')
            start
            ;;
    'stop')
            stop ; echo "Sleeping..."; sleep 1 ;
            start
            ;;
    'status')
            status
            ;;
    'restart')
            stop ; echo "Sleeping..."; sleep 1 ;
            start
            ;;
    *)
            echo
            echo "Usage: $0 { build | start | stop | restart | status }"
            echo
            exit 1
            ;;
esac

exit 0
