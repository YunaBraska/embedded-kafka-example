package com.springfrosch.kafkaexample.kafkaexample.util;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class SpringBootEmbeddedKafka {

    @Autowired
    public KafkaTemplate<String, String> kafka;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, false);

    @BeforeClass
    public static void setUpBeforeClass() {
        //FIXME: Couldn't find kafka server configurations - kafka server is listening on a random port so i overwrite the client config here should be other way around
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
        System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", embeddedKafka.getZookeeperConnectionString());
    }

}
