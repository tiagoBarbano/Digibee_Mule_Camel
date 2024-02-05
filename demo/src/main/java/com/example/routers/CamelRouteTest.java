// package com.example.routers;

// import org.apache.camel.builder.RouteBuilder;
// import org.apache.camel.model.dataformat.JsonLibrary;
// import org.apache.camel.component.redis.RedisConstants;


// public class CamelRouteTest extends RouteBuilder {


//     @Override
//     public void configure() throws Exception {
//         // Configuring a general error handler to handle unhandled exceptions
//         onException(Exception.class)
//             .handled(true)
//             .log("Unhandled exception caught: ${exception.message}")
//             .to("direct:handleGenericError");

//         // Configuring Dead Letter Channel for the startOrderService route
//         errorHandler(deadLetterChannel("direct:handleErrorOrderService")
//             .maximumRedeliveries(3)  // Maximum redelivery attempts
//             .redeliveryDelay(1000));  // Delay between redelivery attempts

//         from("direct:startOrderService")
//             .doTry()
//                 .to("rest:post:/orderService/createOrder")
//                 .process(exchange -> {
//                     // Logic to extract information from the response, adapt as needed
//                     String orderId = "123";  // Replace with logic to obtain orderId
//                     String status = "ORDER_CREATED";  // Replace with the desired status

//                     // Setting the necessary headers
//                     exchange.getIn().setHeader("orderId", orderId);
//                     exchange.getIn().setHeader("status", status);
//                 })
//                 .multicast()
//                     .parallelProcessing()
//                     .to("direct:persistState", "direct:asyncPersistState")
//             .endDoCatch()
//                 .onWhen(simple("${header.CamelHttpResponseCode} == 500"))
//                     .to("direct:handleError500")
//                 .endDoTry();

//         // Route to persist the state in Redis synchronously
//         from("direct:persistState")
//             .marshal().json(JsonLibrary.Jackson)
//             .setHeader(RedisConstants.COMMAND, constant("SET"))
//             .setHeader(RedisConstants.KEY, simple("${header.orderId}"))
//             .to("redis://localhost:6379");

//         // Route to persist the state in Redis asynchronously
//         from("direct:asyncPersistState")
//             .log("Async persistence of order state to Redis")
//             .process(exchange -> {
//                 // Additional logic for asynchronous persistence
//             })
//             .end();

//         // Specific route to handle error with status code 500
//         from("direct:handleError500")
//             .log("Handling 500 error in order service")
//             .to("direct:persistState500")
//             .end();

//         // Route to persist the state in Redis when an error with status code 500 occurs
//         from("direct:persistState500")
//             .setHeader("orderId", simple("${body.orderId}"))  // Adjust according to the message structure
//             .setHeader("status", constant("500_ERROR"))      // Additional information about the error
//             .marshal().json(JsonLibrary.Jackson)
//             .setHeader(RedisConstants.COMMAND, constant("SET"))
//             .setHeader(RedisConstants.KEY, simple("${header.orderId}"))
//             .to("redis://localhost:6379");

//         // Default error handling route for other status codes
//         from("direct:handleErrorOrderService")
//             .log("Handling error in order service")
//             // Add additional logic to handle other errors
//             .end();

//         // Generic route for handling global errors
//         from("direct:handleGenericError")
//             .log("Handling generic error")
//             // Add additional logic to handle unhandled exceptions
//             .end();
        
//         // Other routes and services can be configured similarly
//     }
// }