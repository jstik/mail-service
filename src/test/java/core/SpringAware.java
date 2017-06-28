package core;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.unitils.UnitilsJUnit4TestClassRunner;

import junit.framework.TestCase;

/**
 * Created by Julia on 06.06.2017.
 */
@WebAppConfiguration
@RunWith(UnitilsJUnit4TestClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/resources/spring-email.xml", "file:src/main/resources/spring-db.xml",
		"file:src/test/resources/spring-test-config.xml", "file:src/test/resources/spring-web-test.xml" })
/*
 * @SpringApplicationContext({"spring-email.xml"})
 */
public class SpringAware extends TestCase {
   /* @SpringApplicationContext
    protected ApplicationContext context;*/

	/*
	 * @SpringApplicationContext public ApplicationContext setUpContext(){
	 * System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME,
	 * "test"); return new ClassPathXmlApplicationContext("spring-email.xml",
	 * "spring-test-config.xml", "spring-db.xml", "spring-web-test.xml"); }
	 */

    @Before
    public void init(){

    }
}
