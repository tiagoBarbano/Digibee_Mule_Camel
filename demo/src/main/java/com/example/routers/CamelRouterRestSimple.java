package com.example.routers;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.models.User;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

import java.util.Map;

/**
 * A simple Camel REST DSL route with OpenApi API documentation.
 */
@Component
public class CamelRouterRestSimple extends RouteBuilder {

	@Value("${camel.servlet.mapping.context-path}")
	private String contextPath;

	@Override
	public void configure() throws Exception {

		rest("/users").description("User REST service")
				.consumes("application/json")
				.produces("application/json")

				.get().description("Find all users").outType(User[].class)
				.responseMessage().code(200).message("All users successfully returned")
				.endResponseMessage()
				.to("bean:userService?method=findUsers")

				.get("/{id}").description("Find user by ID")
				.outType(User.class)
				.param().name("id").type(path).description("The ID of the user").dataType("integer")
				.endParam()
				.responseMessage().code(200).message("User successfully returned").endResponseMessage()
				.to("bean:userService?method=findUser(${header.id})")

				.put("/{id}").description("Update a user").type(User.class)
				.param().name("id").type(path).description("The ID of the user to update")
				.dataType("integer")
				.endParam()
				.param().name("body").type(body).description("The user to update").endParam()
				.responseMessage().code(204).message("User successfully updated").endResponseMessage()
				.to("direct:update-user");

		from("direct:update-user")
				.to("bean:userService?method=updateUser")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
				.setBody(constant(""));

		rest("/chamadaProduto")
				.consumes("application/json")
				.produces("application/json")

				.post().description("teste")
				.param().name("body").type(body).endParam()
				.responseMessage().code(500).message("problema no processamento").endResponseMessage()
				.to("direct:processRoute");

		// Rota de processamento
		from("direct:processRoute")
				.log("Recebendo requisição: ${body}")
				.process(exchange -> {
					// Obtenha o valor do campo 'chamada' e armazene em uma propriedade de troca
					String chamada = exchange.getIn().getBody(Map.class).get("chamada").toString();
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
		from("direct:apiPrimeiro")
				.log("Roteando para API Primeiro: ${body}")
				.to("direct:step01");
		// Adicione aqui a lógica para chamar a API correspondente

		// Rota para a API Segundo
		from("direct:apiSegundo")
				.log("Roteando para API Segundo: ${body}");
		// Adicione aqui a lógica para chamar a outra API

		// Rota para a API Primeiro
		from("direct:step01")
				.log("Roteando para step01: ${body}")
				.to("direct:step02");

		// Rota para a API Segundo
		from("direct:step02")
				.log("Roteando para step02 Segundo: ${body}");
		// Adicione aqui a lógica para chamar a outra API

	}

}