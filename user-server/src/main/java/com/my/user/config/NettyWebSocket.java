package com.my.user.config;

import com.alibaba.fastjson.JSONObject;
import com.my.user.socket.MessageType;

import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@CrossOrigin
@ServerEndpoint( path = "/user", port ="9070")
@Component
public class NettyWebSocket {

    private static ConcurrentMap<Long, Session> sessionMap = new ConcurrentHashMap<>(3);
    private static ConcurrentMap<Long, List<Session>> bidSectionIdMap = new ConcurrentHashMap<>(3);

    @OnOpen
    public void onOpen(Session session, @PathVariable String userId, @PathVariable String bidSectionId, @PathVariable Map pathMap){
        //连接 将数据存入map
        session.setAttribute("userId", pathMap.get("userId"));
        sessionMap.put(Long.parseLong(pathMap.get("userId").toString()),session);
        bidSectionIdMap.computeIfAbsent(Long.parseLong(pathMap.get("userId").toString()), k -> new ArrayList<>()).add(session);
            MessageType messageType=new MessageType();

            sendMessage(Long.parseLong(pathMap.get("userId").toString()),messageType);
        }


    @OnClose
    public void onClose(Session session) throws IOException {
        sessionMap.remove(session.getAttribute("userId"));
        bidSectionIdMap.computeIfAbsent(Long.parseLong(session.getAttribute("bidSectionId")), k -> new ArrayList<>()).remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
//        for(Map.Entry<String,Session> entry: sessionMap.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            if (entry.getKey().equals("1")){
//                entry.getValue().sendText(entry.getKey());
//                entry.getValue().close();
//                sessionMap.remove(entry.getKey());
//            }
//        }
//        System.out.println(message+sessionMap.toString());
        session.sendText("Hello Netty!");
        MessageType messageType = new MessageType();
        messageType.setMsg(message);
        messageType.setType("2");
        sendMessage(Long.parseLong(session.getAttribute("userId")), messageType);
        sendAllMessage(Long.parseLong(session.getAttribute("bidSectionId")),messageType);
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            String userId = session.getAttribute("userId");
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println(("30秒未收到用户:"+userId+",地址:"+session.remoteAddress()+"的心跳开始关闭连接"));
                    session.close();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 单独发送
     * @param userId  用户id
     * @param nettyMsgVo  内容
     */
    public void sendMessage(Long userId,  MessageType nettyMsgVo) {
        for(Map.Entry<Long,Session> entry: sessionMap.entrySet()) {
            if (entry.getKey().equals(userId)){
                entry.getValue().sendText(JSONObject.toJSONString(nettyMsgVo));
            }
        }
    }
    /**
     * 根据标段群发
     * @param bidSectionId 标段id
     * @param nettyMsgVo  内容
     */
    public void sendAllMessage(Long bidSectionId, MessageType nettyMsgVo) {
        for (Session session : bidSectionIdMap.computeIfAbsent(bidSectionId, k -> new ArrayList<>())) {
            session.sendText(JSONObject.toJSONString(nettyMsgVo));
        }
    }
}
