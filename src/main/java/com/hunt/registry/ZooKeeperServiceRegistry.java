package com.hunt.registry;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ouyangan on 2017/6/15.
 */
public class ZooKeeperServiceRegistry implements ServiceRegistry {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);

    private ZkClient zkClient;

    private String zkAdress;

    public ZooKeeperServiceRegistry(String zkAdress) {
        this.zkAdress = zkAdress;
        this.zkClient = new ZkClient(this.zkAdress, ZooKeeperConstant.sessionTimeOut, ZooKeeperConstant.connectionTimeOut);
    }

    public void register(String serviceName, String serviceAddress) {
        boolean exists = zkClient.exists(ZooKeeperConstant.registryPath);
        if (!exists) {
            zkClient.createPersistent(ZooKeeperConstant.registryPath);
            log.debug("zooKeeper registry node:{}", ZooKeeperConstant.registryPath);
        }
        String servicePath = ZooKeeperConstant.registryPath + "/" + serviceName;
        boolean servicePathExist = zkClient.exists(servicePath);
        if (!servicePathExist) {
            zkClient.createPersistent(servicePath);
            log.debug("zooKeeper create service node:{}", servicePath);
        }
        zkClient.createEphemeralSequential(servicePath + "/address-", serviceAddress);
        log.debug("zooKeeper create service node:{}", servicePath);

    }
}
