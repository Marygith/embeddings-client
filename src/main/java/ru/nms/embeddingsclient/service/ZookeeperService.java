package ru.nms.embeddingsclient.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nms.embeddingslibrary.model.MasterServiceMeta;

import javax.annotation.PostConstruct;

@Service
public class ZookeeperService {

    @Value("${zookeeper.service.base.path}")
    private String basePath;

    @Value("${zookeeper.service.base.max.retry.policy}")
    private int maxRetries;

    @Value("${zookeeper.service.base.sleep.time}")
    private int sleepTimeInMillis;

    @Value("${zookeeper.master.service.name}")
    private String masterServiceName;

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    private ServiceProvider<MasterServiceMeta> serviceProvider;

    @PostConstruct
    private void init() {
        JsonInstanceSerializer<MasterServiceMeta> serializer = new JsonInstanceSerializer<MasterServiceMeta>(MasterServiceMeta.class);

        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperAddress, new ExponentialBackoffRetry(sleepTimeInMillis, maxRetries));
        client.start();
        ServiceDiscovery<MasterServiceMeta> serviceDiscovery = ServiceDiscoveryBuilder.builder(MasterServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(basePath)
                .watchInstances(true)
                .build();


        try {
            serviceDiscovery.start();
            System.out.println("current services: ");
            serviceDiscovery.queryForNames().forEach(System.out::println);
//            System.out.println("printing services");
//            serviceDiscovery.queryForInstances(masterServiceName).forEach(System.out::println);
            this.serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(masterServiceName)
                    .build();
            this.serviceProvider.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ServiceInstance<MasterServiceMeta> findMainServerInstance() throws Exception {
        return serviceProvider.getInstance(); //default provider strategy - round robin
    }
}
