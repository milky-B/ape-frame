package airport.cargos.springcloud.sku.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sku")
public class SkuController {

    @GetMapping("test")
    public String test() throws InterruptedException {
        Thread.sleep(3000);
        return "hello sku world";
    }

    @HystrixCommand(fallbackMethod = "hystrixFallback",commandProperties={
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    @GetMapping("testHystrix")
    public String testHystrix() throws InterruptedException{
        Thread.sleep(5000);
        return "hello testHystrix";
    }

    public String hystrixFallback(){
        return "3000 timeout default result";
    }
}
