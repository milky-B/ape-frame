import com.airport.ape.redis.util.RedisLockUtil;
import com.airport.ape.redis.util.RedisUtil;
import com.airport.ape.tool.CompletableFutureUtils;
import com.airport.ape.user.UserApplication;
import com.airport.ape.user.delayQueue.MassMailTask;
import com.airport.ape.user.delayQueue.MassMailTaskService;
import com.airport.ape.user.event.Person;
import com.airport.ape.user.event.PersonEventPublisher;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@SpringBootTest(classes = {UserApplication.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
public class MailThreadPoolTest {

    @Autowired
    private PersonEventPublisher personEventPublisher;

    @Autowired
    @Qualifier("mailThreadPool")
    private ThreadPoolExecutor  mailThreadPool;

    @Test
    public void mailThreadPoolTest01(){
        for(int i=0;i<10;i++){
            mailThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    log.info("{}",System.currentTimeMillis());
                }
            });
        }
    }
    @Test
    public void futureUtilsTest(){
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        FutureTask futureTask1 = new FutureTask<String>(()->"aaaaa");
        FutureTask futureTask2 = new FutureTask<String>(()-> {
            Thread.sleep(2000);
            return "bbbbb";
        });

        mailThreadPool.submit(futureTask1);
        mailThreadPool.submit(futureTask2);
        futureTasks.add(futureTask1);
        futureTasks.add(futureTask2);
        for(FutureTask<String> stringFutureTask : futureTasks){
            String name = CompletableFutureUtils.getResult(stringFutureTask,1, TimeUnit.SECONDS,"ccccc",log);
            log.info("testFutureResult{}",name);
        }
    }
    @Test
    public void personEventPublisher(){
        Person person = new Person();
        person.setName("aaa");
        person.setAge(18);
        personEventPublisher.createPersonEvent(person);
    }


    @Autowired
    private MassMailTaskService massMailTaskService;
    @Autowired
    private RedisLockUtil redisLockUtil;
    private static final String DELAY_QUEUE_LOCK_KEY = "DELAY_QUEUE_LOCK_KEY";
    @Test
    public void push() throws ParseException {
        for(int i=0;i<3;i++){
        MassMailTask massMailTask = new MassMailTask();
        massMailTask.setTaskId(1001L+i);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = simpleDateFormat.parse("2023-12-28 14:43:00");
        massMailTask.setStartTime(parse);
        massMailTaskService.publishMassMailTask(massMailTask);
        }
    }
    @Test
    public void pull(){
        String s = UUID.randomUUID().toString();
        try{
            boolean lock = redisLockUtil.lock(DELAY_QUEUE_LOCK_KEY,s , 10000L);
            if(!lock){
                return;
            }
            Set<Long> ids = massMailTaskService.pullMassMailTask();
            log.info("delay task list{}", JSON.toJSON(ids));
            if(CollectionUtils.isEmpty(ids)){
                return;
            }
            for (Long id:ids){
                log.info("正在执行延迟任务:{}",id);
            }

        } catch (Exception e){
            log.error("延迟任务执行失败 {}",e.getMessage(),e);
        }finally {
            redisLockUtil.unlock(DELAY_QUEUE_LOCK_KEY, s);
        }
    }


    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedisZSet(){
        Set delay = redisTemplate.opsForZSet().rangeWithScores("delay", 0L, -1L);
        delay.forEach(System.out::println);
        redisTemplate.expire("qwe",30,TimeUnit.SECONDS);
    }
    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void testRedisEvalCas(){
        redisUtil.evalCas("perm",2L,3L);
    }
}
