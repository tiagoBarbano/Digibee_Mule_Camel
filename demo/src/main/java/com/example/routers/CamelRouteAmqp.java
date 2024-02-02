package com.example.routers;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class CamelRouteAmqp extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // from("timer:hello?period=1000")
        //     .transform(simple("Random number ${random(0,100)}"))
        //     .to("spring-rabbitmq:mykey?routingKey=mykey");

        // from("timer:hello?period=2000")
        //     .transform(simple("Bigger random number ${random(100,200)}"))
        //     .to("spring-rabbitmq:mykey?routingKey=mykey");

        from("spring-rabbitmq:mykey?queues=myqueue2&routingKey=mykey2").routeId("consumer-myqueue2")
            .log("From RabbitMQ: ${body}");
    }
}
