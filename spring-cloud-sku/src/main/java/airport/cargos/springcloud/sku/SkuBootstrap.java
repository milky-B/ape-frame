package airport.cargos.springcloud.sku;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages= {"com.airport.ape.user.mapper"})
@ComponentScan({"airport.cargos","com.airport.ape"})
@EnableCaching
//@EnableEurekaClient
//@EnableDiscoveryClient
@EnableHystrix
public class SkuBootstrap {
    public static void main(String[] args) {
        //配置异步日志上下文选择器,不配置的话，等于没开异步
        System.setProperty("log4jContextSelector","org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SpringApplication.run(SkuBootstrap.class);
    }
}