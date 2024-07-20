package com.airport.ape.redis.init;

import com.airport.ape.redis.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InitCache implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        //找出所有实现了AbstractCache的实体类进行预热
        //实体类被Spring管理，去springContext找
        ApplicationContext applicationContext = SpringContextUtil.applicationContext;
        Map<String, AbstractCache> beanMap = applicationContext.getBeansOfType(AbstractCache.class);
        for (Map.Entry <String,AbstractCache> bean : beanMap.entrySet()){
            applicationContext.getBean(bean.getValue().getClass()).initCache();
        }
    }
}
