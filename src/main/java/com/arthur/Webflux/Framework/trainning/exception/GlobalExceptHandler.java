package com.arthur.webflux.framework.trainning.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import java.util.Map;
import java.util.Optional;

@Component
@Order(-2)
public class GlobalExceptHandler extends AbstractErrorWebExceptionHandler {

    /**
     * Parâmetros do Construtor:
     *
     * ErrorAttributes errorAttributes: Contém informações sobre os erros que ocorrem na aplicação. Essas informações são usadas para gerar respostas de erro detalhadas.
     * WebProperties webProperties: Contém propriedades de configuração da aplicação web, como recursos estáticos, entre outras.
     * ApplicationContext applicationContext: O contexto da aplicação Spring, que fornece acesso a beans e outros recursos da aplicação.
     * ServerCodecConfigurer codecConfigurer: Configurações para codificação e decodificação de mensagens (por exemplo, JSON, XML) no servidor.
     * @param errorAttributes
     * @param webProperties
     * @param applicationContext
     * @param codecConfigurer
     */
    public GlobalExceptHandler(ErrorAttributes errorAttributes,
                               WebProperties webProperties,
                               ApplicationContext applicationContext,
                               ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    /**
     * Anotação @Override: Indica que este método está sobrescrevendo um método da superclasse.
     * Método getRoutingFunction: Este método define a função de roteamento para erros.
     * Parâmetro errorAttributes: Este parâmetro contém os atributos de erro que foram gerados pela aplicação.
     * Retorno RouterFunctions.route: Este método cria uma rota que captura todas as requisições (RequestPredicates.all()) e as redireciona para o método formatErrorResponse.
     * @param errorAttributes
     * @return
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest request){
        Map<String, Object> errorAttributesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (int) Optional.ofNullable(errorAttributesMap.get("status")).orElse(500);

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }
}
