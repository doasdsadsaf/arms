package com.my.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
// 定义这个过滤器执行顺序 越小顺序越高
//@Order(-1)
public class MyGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 处理当前请求，如果有必要的话 可以交给下一个过滤器 chain
     * @param exchange 请求的上下文，可以获取 request response 信息
     * @param chain 下一个过滤器
     * @return 标示当前过滤器业务结束
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = request.getQueryParams();
        // 2. 获取authorization 参数
        String auth = params.getFirst("authorization");
        // 3。 判断参数值是否等于admin
        if("admin".equals(auth)){
            // 4. 是就放行
            return chain.filter(exchange);
        }
        // 5. 否则拦截
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    // 也可以继承orderd接口 实现Order方法 来确定过滤器执行顺序
    @Override
    public int getOrder() {
        return -1;
    }
}
