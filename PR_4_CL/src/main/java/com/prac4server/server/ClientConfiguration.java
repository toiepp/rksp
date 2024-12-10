package com.prac4server.server;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.util.retry.Retry;

import java.net.InetSocketAddress;
import java.time.Duration;

@Configuration
public class ClientConfiguration {
    @Bean
    public RSocketStrategies clientRSocketStrategies() {
        return RSocketStrategies.builder()
                .encoder(new Jackson2JsonEncoder())
                .decoder(new Jackson2JsonDecoder())
                .build();
    }

    @Bean
    public RSocketRequester getRSocketRequester() {
        return RSocketRequester.builder()
                .rsocketStrategies(clientRSocketStrategies())
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 5200);
    }
}