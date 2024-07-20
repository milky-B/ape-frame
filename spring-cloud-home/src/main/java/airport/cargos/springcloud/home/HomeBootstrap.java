package airport.cargos.springcloud.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"airport.cargos","com.airport.ape"})
@EnableCaching
@EnableEurekaClient
//@EnableDiscoveryClient
//@RibbonClient(name = "eureka-client-sku",configuration = RuleConfig.class)
//@LoadBalancerClient
@EnableFeignClients
@EnableHystrix
public class HomeBootstrap {
    public static void main(String[] args) {
        //配置异步日志上下文选择器,不配置的话，等于没开异步
        System.setProperty("log4jContextSelector","org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SpringApplication.run(HomeBootstrap.class);
    }
}