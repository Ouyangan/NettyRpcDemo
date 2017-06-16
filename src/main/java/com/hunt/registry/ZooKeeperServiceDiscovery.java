package com.hunt.registry;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ZooKeeperServiceDiscovery implements ServiceDiscovery {
    private static final Logger log = LoggerFactory.getLogger(ZooKeeperServiceDiscovery.class);

    private String zkAdress;
    private ZkClient zkClient;

    public ZooKeeperServiceDiscovery(String zkAdress) {
        this.zkAdress = zkAdress;
        this.zkClient = new ZkClient(this.zkAdress, ZooKeeperConstant.sessionTimeOut, ZooKeeperConstant.connectionTimeOut);

    }

    public String discover(String serviceName) {
        String servicePath = ZooKeeperConstant.registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            throw new RuntimeException(String.format("zooKeeper can not found any service on path:", servicePath));
        }
        List<String> children = zkClient.getChildren(servicePath);
        if (children.size() == 0) {
            throw new RuntimeException("zookeeper can not fund any service child node " + servicePath);
        }
        String address = "";
        if (children.size() == 1) {
            address = children.get(0);
        } else {
            address = children.get(ThreadLocalRandom.current().nextInt(children.size()));
        }
        String path = zkClient.readData(servicePath + "/" + address);
        return path;
    }
}
