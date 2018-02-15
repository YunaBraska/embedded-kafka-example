package com.springfrosch.kafkaexample.kafkaexample.controller;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerClassRuleTest {

    @Autowired
    private Receiver receiver;

    @Autowired
    private Sender sender;

    @Value("${kafkatest.topic}")
    private String topic;

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

    @Test
    public void testReceive() throws Exception {
        kafka.send(topic, "Sending with default template");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount(), equalTo(0L));
    }

    @Test
    public void testSend() throws Exception {
        sender.send(topic, "Sending with own controller");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount(), equalTo(0L));
    }
}