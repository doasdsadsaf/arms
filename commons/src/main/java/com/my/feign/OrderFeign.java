package com.my.feign;

import com.alibaba.fastjson.JSONObject;
import com.my.fallback.OrderClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @创建人 dw
 * @创建时间 2021/12/17
 * @描述
 */
@FeignClient(value = "order-server", path = "order",fallbackFactory = OrderClientFallbackFactory.class)
@RequestMapping
public interface OrderFeign {

    @RequestMapping("/show")
    @ResponseBody
    JSONObject show(@RequestBody JSONObject json);


}
