package airport.cargos.springcloud.home.controller;

import airport.cargos.springcloud.home.feign.SkuFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("home")
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;
    private static final String GET_SKU_INFO_URL = "http://EUREKA-CLIENT-SKU/sku/test";
    @Autowired
    private SkuFeignService skuFeignService;

    @GetMapping("home")
    public String test(){
        return "hello home world";
    }
    @GetMapping("testRest")
    public String testRest(){
        return restTemplate.getForObject(GET_SKU_INFO_URL, String.class);
    }
    @GetMapping("testFeign")
    public String testFeign() {
        return skuFeignService.test();
    }
    @GetMapping("testHystrix")
    @HystrixCommand(fallbackMethod = "fallbackTest",
            commandProperties = @HystrixProperty(
                    name="execution.isolation.thread.timeoutInMilliseconds",
                    value = "1000"))
    public String testHystrix(){
        return skuFeignService.testHystrix();
    }
    public String fallbackTest(){
        return "1000 timeout default result";
    }
}
