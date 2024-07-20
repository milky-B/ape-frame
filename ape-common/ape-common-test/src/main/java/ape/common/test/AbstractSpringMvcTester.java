package ape.common.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * ***************************************************
 *
 * @Author Y0ung
 * @Date 2016/6/14 9:52
 * @Description create for solitaire-api
 * ***************************************************
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = {Bootstrap.class,AbstractSpringMvcTester.TestConfiguration.class},webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
public abstract class AbstractSpringMvcTester {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Configuration
    public static class TestConfiguration {

    }

    @Before
    final public void initMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(new MockMvcConfigurerAdapter() {
                    @Override
                    public RequestPostProcessor beforeMockMvcCreated(ConfigurableMockMvcBuilder<?> builder, WebApplicationContext cxt) {
                        return new RequestPostProcessor() {
                            @Override
                            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                                return request;
                            }
                        };
                    }
                })
                .build();
    }

    protected<T> T mockRequest(MockHttpServletRequestBuilder requestBuilder,TypeReference<T> typeReference) throws Exception {
        String contentAsString = mockMvc.perform(requestBuilder).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andDo(MockMvcResultHandlers.print()).
                //andExpect(MockMvcResultMatchers.jsonPath("code").value(1)).
                andReturn().
                getResponse().
                getContentAsString();
        T t = JSONObject.parseObject(contentAsString, typeReference);
        return t;
    }

    protected static String buildUrl(String url, List<Pair> pairList) {
        if (!CollectionUtils.isEmpty(pairList)) {
            StringJoiner stringJoiner = new StringJoiner("&");
            pairList.forEach(pair -> {
                Object value = pair.getValue();
                if (value != null && ((value instanceof String && StringUtils.hasLength((String) value)) || !(value instanceof String))) {
                    stringJoiner.add(pair.getKey() + "=" + pair.getValue());
                }
            });
            url += "?" + stringJoiner;
        }
        return url;
    }

    protected static <T> String buildRandomTestData(T object, Object... vars) {
        System.out.println(object.getClass().getName());
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < vars.length; i += 2) {
            if (!(vars[i] instanceof String)) {
                throw new IllegalArgumentException("check vars type");
            }
            map.put((String) vars[i], vars[i + 1]);
        }
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                    continue;
                }
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    field.set(object, map.get(field.getName()));
                }else{
                    field.set(object,randomData(field.getType()));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return JSONObject.toJSONString(object);
    }

    private static Object randomData(Class<?> type) {
        if(type == String.class){
            return "test_"+generateRandomString();
        }else if(type == Integer.class){
            return new Random().nextInt(100);
        }else if(type == Long.class){
            return new Random().nextLong();
        }else if(type==Float.class){
            return new Random().nextFloat();
        }
        return null;
    }
    private static String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvwxyz123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
