package com.hunt.service;

import com.hunt.model.Person;

/**
 * Created by ouyangan on 2017/6/15.
 */
public interface HelloService {
    String hello(String name);

    Person insert(Person person);
}
