package com.microservice.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {
    private Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(NameConfig.class);

        this.env = env;
    }


    @Override
    public GatewayFilter apply(NameConfig config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)
            ) {
                return onError(
                        exchange,
                        "No authorization header",
                        HttpStatus.UNAUTHORIZED
                );
            }

            String authorizationHeader = request.getHeaders()
                    .get(HttpHeaders.AUTHORIZATION)
                    .get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)) {
                return onError(
                        exchange,
                        "JWT token is not valid",
                        HttpStatus.UNAUTHORIZED
                );
            }

            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean _valid = true;
        String subject = null;

        try {
            subject = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch(Exception ex) {
            _valid = false;
        }

        if(subject == null || subject.isEmpty()) {
            _valid = false;
        }

        return _valid;
    }

    private Mono<Void> onError(
            ServerWebExchange exchange,
            String err,
            HttpStatus httpStatus
    ) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

}
