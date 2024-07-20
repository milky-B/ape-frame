import ape.common.test.AbstractSpringMvcTester;
import com.airport.ape.user.UserApplication;
import com.airport.ape.user.entity.po.Classes;
import com.airport.ape.user.mapper.ClassMapper;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApplication.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClassMapperTest extends AbstractSpringMvcTester {
    @Autowired
    private ClassMapper classMapper;
    @Test
    public void classMapperTest(){
        List<Classes> classes = classMapper.selectClasses();
        classes.forEach(System.out::println);
    }
    @Test
    public void mockTest() throws Exception {
        Object url = mockRequest(MockMvcRequestBuilders.get("url")
                , new TypeReference<Object>() {
        });

    }
}
