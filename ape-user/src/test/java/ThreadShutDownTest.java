import com.airport.ape.tool.PropertiesUtils;
import com.airport.ape.tool.ShutDownThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadShutDownTest {

    @Test
    public void testThreadShutDown() throws InterruptedException {
        ExecutorService mailThreadPool = Executors.newFixedThreadPool(20);

        for(int i=0;i<100;i++){
            mailThreadPool.execute(new ThreadShutDown());
        }
        Thread.sleep(1000);
        log.info("thread shutdown :{} before",mailThreadPool.isShutdown());
        List<Runnable> runnables = mailThreadPool.shutdownNow();
        Thread.sleep(5000);
        log.info("thread shutdown :{} after",mailThreadPool.isTerminated());
        Thread.sleep(500);
        log.info("thread shutdown");
        mailThreadPool.execute(new ThreadShutDown());
    }

    class ThreadShutDown implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(500);
                log.info("ThreadShutDown{}",Thread.currentThread().getName());
            }catch (InterruptedException e){
                log.info("thread interruptedException :{}",e.getMessage());
            }
        }
    }

    @Test
    public void testThreadShutDownUtils(){
        ExecutorService mailThreadPool = Executors.newFixedThreadPool(20);

        for(int i=0;i<100;i++){
            mailThreadPool.execute(new ThreadShutDown());
        }

        ShutDownThreadPoolUtil .shutDownPool(mailThreadPool,1000L,1000L,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testPath(){
        String dir = System.getProperty("user.dir") + File.separator + "shortPropertyFileName" + ".properties";
        System.out.println(dir);
        URL resource = PropertiesUtils.class.getResource("/" + "fileName" + ".properties");
        System.out.println(resource);
    }

    @Test
    public void testPropertiesFileUtils(){
        PropertiesUtils instance = PropertiesUtils.getInstance();
        String propertyValue = instance.getPropertyValue("test1", "name");
        System.out.println(propertyValue);
    }
}
