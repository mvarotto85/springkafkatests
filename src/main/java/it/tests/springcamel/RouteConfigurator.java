package it.tests.springcamel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RouteConfigurator extends RouteBuilder {

    @Value("${kafka.bootstrap-servers}")
    public String brokerAddress;

    @Autowired
    private MessageSenderService messageSenderService;

    public static final String DIRECT_SEND_TO_KAFKA = "direct:send_to_kafka";

    @Override
    public void configure() throws Exception {
        from("timer://foo?fixedRate=true&period=5000")
            .process(e -> e.getIn().setBody(new String("this message should be sent to a queue")))
            .log("${body}")
            .to("bean:messageSenderService");

    }
}
