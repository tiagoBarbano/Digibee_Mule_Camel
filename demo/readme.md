EXERCICIO CAMEL

*** 

Start aplicacao:

        mvn clean package -DskipTests
        mvn spring-boot:run

***

* http://localhost:10001/actuator/hawtio/camel/routes

* http://localhost:10001/actuator

* http://localhost:8080/api/api-doc

* http://localhost:8080/api/users

* http://localhost:8080/api/chamadaProduto

***

        curl  -X POST \
        'http://localhost:8080/api/chamadaProduto' \
        --header 'Accept: */*' \
        --header 'User-Agent: Thunder Client (https://www.thunderclient.com)' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "chamada" : "primeiro",
        "teste": 1234
        }'