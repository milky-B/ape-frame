package com.airport.ape.spring.cloud.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;

/*
* 继承AbstractLoadBalancerRule自定义策略,选出端口号第一个大8888的server
* */
public class MyCustomRule extends AbstractLoadBalancerRule {
    public MyCustomRule() {
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @SuppressWarnings({"RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE"})
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();
            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }
            for(Server s:upList){
                if(s.getPort()>8888){
                    server=s;
                    break;
                }
            }
            if (server == null) {
                Thread.yield();
            } else {
                if (server.isAlive()) {
                    return server;
                }
                server = null;
                Thread.yield();
            }
        }
        System.out.println("host:"+server.getHost()+"port:"+server.getPort());
        return server;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

}
