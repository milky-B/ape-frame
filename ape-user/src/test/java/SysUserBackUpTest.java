import ape.common.test.AbstractSpringMvcTester;
import com.airport.ape.user.UserApplication;
import com.airport.ape.web.entity.Result;
import com.airport.ape.web.entity.ResultCode;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {UserApplication.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysUserBackUpTest extends AbstractSpringMvcTester {
    @Test
    public void testBackUp() throws Exception {
         
        Result responseEntity = mockRequest(MockMvcRequestBuilders.get("/sysUser/backUp"), new TypeReference<Result>() {
        });
        Assert.assertEquals(responseEntity.getResultCode(), ResultCode.SUCCESS);
    }
}
