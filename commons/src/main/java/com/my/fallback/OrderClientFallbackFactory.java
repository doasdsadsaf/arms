package com.my.fallback;

import com.alibaba.fastjson.JSONObject;
import com.my.feign.OrderFeign;
import feign.Logger;
import feign.hystrix.FallbackFactory;
import org.springframework.core.annotation.Order;

/**
 * @创建人 dw
 * @创建时间 2021/12/23
 * @描述
 */
public class OrderClientFallbackFactory implements FallbackFactory<OrderFeign> {
    @Override
    public OrderFeign create(Throwable throwable) {
        return new OrderFeign() {
            @Override
            public JSONObject show(JSONObject json) {
                json.put("error","访问失败");
                return json;
            }
        };

        };
    }

