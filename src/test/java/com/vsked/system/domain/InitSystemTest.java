package com.vsked.system.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class InitSystemTest {

    private static final Logger log = LoggerFactory.getLogger(InitSystemTest.class);

    @Test
    public void init() {
        log.info("init system");
    }

    public void initResource() {
        log.info("init resource");
    }

}
