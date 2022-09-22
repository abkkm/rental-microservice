-- run service :
    => cd to folder: mvn spring-boot:run
-- run rabbit-mq:
    rabbitmq-server start
-- run kafka:
    step 1 : config java to path(i'm using java 11)
    cd folder kafka :
        bin/zookeeper-server-start.sh config/zookeeper.properties
        bin/kafka-server-start.sh config/server.properties

-- run zipkin:
    cd folder zipkin:
        java -jar zipkin.jar


In folder rental :
cd discovery-service
mvn spring-boot:run
cd ..
cd apigateway-service
mvn spring-boot:run
cd ..
cd config-server
mvn spring-boot:run
cd ..
cd auth-service
mvn spring-boot:run
cd..
cd posts-service
mvn spring-boot:run
cd..


