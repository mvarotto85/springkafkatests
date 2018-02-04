package it.tests.springcamel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage(String message){
        System.out.println("sending message to kafka!!");
        kafkaTemplate.send("test",message);
    }
}
