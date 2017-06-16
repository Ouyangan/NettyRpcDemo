import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ouyangan on 2017/6/15.
 */

public class ServerTest {
    private static final Logger log = getLogger(ServerTest.class);

    @Test
    public void startUp() {
        log.debug("start server");
        new ClassPathXmlApplicationContext("spring-server.xml");
    }
}
