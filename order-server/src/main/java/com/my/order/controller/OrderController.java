package com.my.order.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @创建人 dw
 * @创建时间 2021/12/17
 * @描述
 */
@RequestMapping("/order")
@Controller
public class OrderController {

    @RequestMapping("/show")
    @ResponseBody
    public JSONObject show(@RequestBody JSONObject json) {
        return json;
    }


}
