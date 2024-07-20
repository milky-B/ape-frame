import ape.common.test.AbstractSpringMvcTester;
import com.airport.ape.user.UserApplication;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {UserApplication.class,AbstractSpringMvcTester.TestConfiguration.class},webEnvironment = SpringBootTest.WebEnvironment.MOCK)

public class MyControllerTest extends AbstractSpringMvcTester {
    @Test
    public void testIpUtil() throws Exception {
        String ip = mockRequest(MockMvcRequestBuilders.get("/ip"), new TypeReference<String>() {
        });
    }
}
