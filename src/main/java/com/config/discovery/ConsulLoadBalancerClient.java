package com.config.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

/**
 * Created by Julia on 25.07.2017.
 */
@Component("consul")
public class ConsulLoadBalancerClient implements LoadBalancerClient {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private org.springframework.cloud.client.loadbalancer.LoadBalancerClient client;

    @Autowired
    @Qualifier("local")
    private LoadBalancerClient loadBalancerClient;

    @Override
    public ServiceInstance choose(String serviceId) {

        ServiceInstance instance = client.choose(serviceId);
        if (instance == null) {
            logger.debug("Fall to Local properties");
            return loadBalancerClient.choose(serviceId);
        }
        return instance;
    }
}
