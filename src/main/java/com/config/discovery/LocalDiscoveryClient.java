package com.config.discovery;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * Created by Julia on 25.07.2017.
 */
@Component("local")
public class LocalDiscoveryClient implements LoadBalancerClient {
    @Resource
    private Environment environment;

    @Override
    public ServiceInstance choose(String serviceId) {
        String host = environment.getProperty(serviceId + ".host");
        if (host == null) {
            throw new IllegalArgumentException(MessageFormat.format("Check settings for {0}", serviceId));
        }
        Integer port;
        try {
            port = Integer.parseInt(environment.getProperty(serviceId + ".port"));
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageFormat.format("Check settings for {0}", serviceId), e);
        }

        return new DefaultServiceInstance(serviceId, host, port, false);
    }
}
