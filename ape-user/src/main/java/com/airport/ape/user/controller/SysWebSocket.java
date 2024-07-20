package com.airport.ape.user.controller;

//import com.airport.ape.websocket.config.WebSocketCheckConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
//@ServerEndpoint(value = "/sys/socket", configurator = WebSocketCheckConfig.class)
//@Component
public class SysWebSocket {
    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    private static Map<String, SysWebSocket> clients = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 当前会话的唯一标识key
     */
    private String erp = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) throws IOException {
        //获取用户信息
        try {
            // 获取用户配置
            Map<String, Object> userProperties = conf.getUserProperties();
            // 从用户配置中获取"erp"的用户内容
            String erp = (String) userProperties.get("erp");
            this.erp = erp;
            this.session = session;
            // 如果在线的客户端中存在这个用户，则先关闭下线
            if (clients.containsKey(this.erp)) {
                clients.get(this.erp).session.close();
                clients.remove(this.erp);
                onlineCount.decrementAndGet();
            }
            // 将当前用户再连接上去
            clients.put(this.erp, this);
            onlineCount.incrementAndGet();
            log.info("有新连接加入：{}，当前在线人数为：{}", erp, onlineCount.get());
            sendMessage("连接成功", this.session);
        } catch (Exception e) {
            log.error("建立链接错误{}", e.getMessage(), e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            if (clients.containsKey(erp)) {
                clients.get(erp).session.close();
                clients.remove(erp);
                onlineCount.decrementAndGet();
            }
            log.info("有一连接关闭：{}，当前在线人数为：{}", this.erp, onlineCount.get());
        } catch (Exception e) {
            log.error("连接关闭错误，错误原因{}", e.getMessage(), e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端[{}]的消息:{}", this.erp, message);
        // 模拟心跳机制：
        //    前端可以通过setInterval定时任务每个15秒钟调用一次reconnect函数
        //    reconnect会通过socket.readyState来判断这个websocket连接是否正常
        //    如果不正常就会触发定时连接，每15s钟重试一次，直到连接成功
        if (message.equals("ping")) {
            this.sendMessage("pong", session);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Socket:{},发生错误,错误原因{}", erp, error.getMessage(), error);
        try {
            session.close();
        } catch (Exception e) {
            log.error("onError.Exception{}", e.getMessage(), e);
        }
    }

    /**
     * 指定发送消息
     */
    public void sendMessage(String message, Session session) {
        log.info("服务端给客户端[{}]发送消息：{}", this.erp, message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("{}发送消息发生异常，异常原因{}", this.erp, message);
        }
    }

    /**
     * 群发消息
     */
    public void sendMessage(String message) {
        for (Map.Entry<String, SysWebSocket> sessionEntry : clients.entrySet()) {
            String erp = sessionEntry.getKey();
            SysWebSocket socket = sessionEntry.getValue();
            Session session = socket.session;
            log.info("服务端给客户端[{}]发送消息{}", erp, message);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("{}发送消息发生异常，异常原因{}", this.erp, message);
            }
        }
    }
}
