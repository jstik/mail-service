package core;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;

/**
 * Created by Julia on 06.06.2017.
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)/*
@SpringApplicationContext({"spring-email.xml"})*/
public class SpringAware extends TestCase {
   /* @SpringApplicationContext
    protected ApplicationContext context;*/

    @SpringApplicationContext
    public ApplicationContext setUpContext(){
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
       return  new ClassPathXmlApplicationContext("spring-email.xml",
               "spring-test-config.xml", "spring-db.xml");
    }

    @Before
    public void init(){

    }
}
