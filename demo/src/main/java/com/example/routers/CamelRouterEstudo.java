package com.example.routers;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

import java.util.Map;

@Component
public class CamelRouterEstudo extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // from("quartz:cron?cron={{quartz.cron}}").routeId("cron")
        //     .setBody().constant("Hello Camel! - cron")
        //     .to("stream:out")
        //     .to("mock:result");

        // from("quartz:simple?trigger.repeatInterval={{quartz.repeatInterval}}").routeId("simple")
        //     .setBody().constant("Hello Camel! - simple")
        //     .to("stream:out")
        //     .to("mock:result");

        // from("timer:hello?period=1000")
        //     .transform(simple("Random number ${random(0,100)}"))
        //     .to("spring-rabbitmq:mykey?routingKey=mykey");

        // from("timer:hello?period=2000")
        //     .transform(simple("Bigger random number ${random(100,200)}"))
        //     .to("spring-rabbitmq:mykey?routingKey=mykey");

        // from("spring-rabbitmq:mykey?queues=myqueue&routingKey=mykey").routeId("consumer-myqueue2")
        //     .log("From RabbitMQ: ${body}");


        // Rota de entrada da api
        rest("/chamadaProduto").id("chamadaProduto")
            .description("chamadaProduto")
            .consumes("application/json")
            .produces("application/json")
            .post().description("teste")
                .param().name("body").type(body).endParam()
                .to("direct:processRoute")
            .get("/{id}").description("Teste Get")
                .param().name("id").type(path).description("The ID").dataType("integer").endParam()
                .to("direct:getHelloWorldFastApi");


		// Rota de processamento
		from("direct:processRoute").routeId("processRoute")
				.log("Recebendo requisição: ${body}")
				.process(exchange -> {
					// Map chamada = exchange.getMessage().getBody(Map.class);
					String chamada = exchange.getMessage().getBody(Map.class).get("chamada").toString();
					exchange.setProperty("chamadaValue", chamada);
				})
				.choice()
					.when().simple("${exchangeProperty.chamadaValue} == 'primeiro'")
						.to("direct:apiPrimeiro")
					.when().simple("${exchangeProperty.chamadaValue} == 'segundo'")
						.to("direct:apiSegundo")
					.otherwise()
						.log("Chamada não reconhecida: ${exchangeProperty.chamadaValue}")
				.end();

		// Rota para a API Primeiro
		from("direct:apiPrimeiro").routeId("apiPrimeiro")
				.log("Roteando para API Primeiro: ${body}")
				.to("direct:step01");
		// Adicione aqui a lógica para chamar a API correspondente

		// Rota para a API Segundo
		from("direct:apiSegundo").routeId("apiSegundo")
				.log("Roteando para API Segundo: ${body}");
		// Adicione aqui a lógica para chamar a outra API

		// Rota para a API Primeiro
		from("direct:step01").routeId("step01")
				.log("Roteando para step01: ${body}")
				.to("direct:step02");

		// Rota para a API Segundo
		from("direct:step02").routeId("step02")
				.log("Roteando para step02 Segundo: ${body}")
				.to("direct:getHelloWorldFastApi");


		from("direct:getHelloWorldFastApi").routeId("getHelloWorldFastApi")
			.removeHeaders("*")
			.setHeader(Exchange.HTTP_METHOD, constant("GET"))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
			.to("http://localhost:8000/")
			.log("Pedido processado [${header.CamelHttpResponseCode}-${header.CamelHttpResponseText}-${body}]")
			.process(exchange -> {
				String corpoOriginal = exchange.getIn().getBody(String.class);
				String corpoModificado = corpoOriginal.substring(0, corpoOriginal.length() - 1) + ",\"nome\":\"teste\"}";
				exchange.getIn().setBody(corpoModificado);
			});
			// .end();

    }

}
