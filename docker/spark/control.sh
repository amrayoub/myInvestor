#!/bin/bash

DOCKER_USER_NAME=mengwangk
BUILD_TAG=$DOCKER_USER_NAME/myinvestor-spark
RUN_NAME=myinvestor-engine
SPARK_NETWORK=spark-network


build() {
    echo
    echo "==== build ===="

    sudo docker-compose build 

    echo   
}

start() {

    echo
    echo "==== start ===="
    sudo network create $SPARK_NETWORK
    sudo docker-compose up -d && docker-compose scale slave=1; 

    echo    
}

stop() {

    echo
    echo "==== stop ===="

    sudo docker-compose down
    sudo docker network rm $SPARK_NETWORK

    echo    
}

status() {

    echo
    echo "==== status ===="

    sudo docker ps -a | grep $RUN_NAME

    echo    
}

shell() {
    echo
    echo "==== shell ===="

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
    'push')
            push 
            ;;
    'command')
            command
            ;;
    *)
            echo
            echo "Usage: $0 { build | start | stop | restart | status | push | command }"
            echo
            exit 1
            ;;
esac

exit 0
