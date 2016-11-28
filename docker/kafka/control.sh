#!/bin/bash

DOCKER_HUB_USER=mengwangk
BUILD_TAG=$DOCKER_HUB_USER/myinvestor-kafka
RUN_NAME=myinvestor-streaming

build() {
    echo
    echo "==== build ===="

    sudo docker build -t $BUILD_TAG .   

    echo   
}

start() {

    echo
    echo "==== start ===="

    sudo docker run --name $RUN_NAME -d -p 2181:2181 -p 9092:9092 $BUILD_TAG
	#--env ADVERTISED_HOST=`sudo docker-machine ip \`sudo docker-machine active\`` --env ADVERTISED_PORT=9092 $BUILD_TAG

    echo    
}

stop() {

    echo
    echo "==== stop ===="

    sudo docker stop $RUN_NAME   
    sudo docker rm $RUN_NAME   

    echo    
}

status() {

    echo
    echo "==== status ===="

    sudo docker ps -a | grep $RUN_NAME

    echo    
}

command() {

    echo
    echo "==== command ===="

    sudo docker exec -it $RUN_NAME bash
	
    echo
}

push(){
    echo
    echo "==== push ===="
    TAG=`sudo docker images | grep $BUILD_TAG | awk '{print $3}'`
    echo "Pushing $TAG to Docker Hub"
    #sudo docker tag $TAG $DOCKER_HUB_USER/$BUILD_TAG:latest
    sudo docker push $BUILD_TAG

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
            stop 
            ;;
    'status')
            status
            ;;
    'restart')
            stop ; echo "Sleeping..."; sleep 1 ;
            start
            ;;
    'command')
            command
            ;;
    'push')
            push 
            ;;
    *)
            echo
            echo "Usage: $0 { build | start | stop | restart | status | command | push }"
            echo
            exit 1
            ;;
esac

exit 0
