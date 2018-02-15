package com.springfrosch.kafkaexample.kafkaexample.controller;

import com.springfrosch.kafkaexample.kafkaexample.util.SpringBootEmbeddedKafka;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ControllerTest extends SpringBootEmbeddedKafka {

    @Autowired
    private Receiver receiver;

    @Autowired
    private Sender sender;

    @Value("${kafkatest.topic}")
    private String topic;

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