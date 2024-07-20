package com.airport.ape.websocket.config;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketCheckConfig extends ServerEndpointConfig.Configurator {
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 在这里进行验证，确保 URL 中包含 "erp" 参数
        String erpParameter = request.getParameter("erp");
        if (!StringUtils.hasLength(erpParameter)) {
            // 如果没有 "erp" 参数，拒绝连接
            return false;
        }
        //校验逻辑
        //request.xxx();
        return true;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Map<String, List<String>> map = request.getParameterMap();
        List<String> erpList = map.getOrDefault("erp", null);
        if(!CollectionUtils.isEmpty(erpList)){
            // 使用ServerEndpointConfig来进行相应的参数传递(此处将erp相关参数信息作为用户参数传递)
            sec.getUserProperties().put("erp", erpList.get(0));
        }
    }
}
