package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StatClient {
    final RestClient client;
    final String statUrl;

    @Autowired
    public StatClient(RestClient client, @Value("${client.url") String startUrl) {
        this.client= client;
        this.statUrl = startUrl;
    }

    protected
    String result = restClient.get()
            .uri(uriBase + "/articles")
            .retrieve()
            .body(String.class);
}
