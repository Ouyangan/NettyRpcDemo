import com.hunt.client.RpcClient;
import com.hunt.model.Person;
import com.hunt.service.HelloService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

public class ClientTest {
    @Test
    public void clientTest() throws InterruptedException {
        StopWatch watch = new StopWatch();
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        RpcClient client = context.getBean(RpcClient.class);
        HelloService helloService = client.create(HelloService.class);
        Person person = new Person();
        person.setId(2L);
        person.setName("an");
        person.setAddress("深圳南山");
        person.setAge(25);
        person.setDesc("java开发");
        person.setSex(2);
        for (int i = 0; i < 100; i++) {
            watch.start("第"+(i+1)+"次");
            Person insert = helloService.insert(person);
            System.out.println(insert);
            watch.stop();
        }
        System.out.println(watch.prettyPrint());

    }
}
