package com.hunt.service;

import com.hunt.model.Person;
import com.hunt.server.RpcService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    private static final Logger log = getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        log.debug("接收到参数:{}", name);
        return "hello:" + name;
    }

    @Override
    public Person insert(Person person) {
        System.out.println(person);
        return person;
    }
}
