package com.config.discovery;

import org.springframework.cloud.client.ServiceInstance;

/**
 * Created by Julia on 25.07.2017.
 */
public interface LoadBalancerClient {
    ServiceInstance choose(String serviceId);
}
