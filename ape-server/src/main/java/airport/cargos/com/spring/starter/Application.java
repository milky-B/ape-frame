package airport.cargos.com.spring.starter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("airport.cargos.com.spring.starter.mapper")
@ComponentScan({"com.airport.ape","airport.cargos.com.spring.starter"})
public class Application {
    public static void main(String[] args) {
        //配置异步日志上下文选择器,不配置的话，等于没开异步
        System.setProperty("log4jContextSelector","org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SpringApplication.run(Application.class);
    }
}
