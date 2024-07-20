package airport.cargos.springcloud.home.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "eureka-client-sku",path = "sku")
public interface SkuFeignService {

    @GetMapping("test")
    String test();

    @GetMapping("testHystrix")
    String testHystrix();
}
