package com.example.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter {
    private String request;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("11111111111111");
        String path = String.valueOf(exchange.getRequest().getPath());
        path=path.replace("/","");
        System.out.println("path.."+path);

        if(!path.equals("test")&&!path.isEmpty()){
            return Mono.empty();
        }

        return chain.filter(exchange);
    }
}
