package com.sovon9.MCPServer.controller;

import com.sovon9.MCPServer.ProductionRun;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
public class TestController {

    public WebClient webClient;

    public TestController()
    {
        webClient = WebClient.builder().
                baseUrl("http://localhost:8081/api").build();
    }

    @GetMapping("/test")
    public List<ProductionRun> getLinePerformance()
    {
        List<ProductionRun> linePerformance = webClient.get().uri(uriBuilder -> uriBuilder.path("/production/line/" + "LINE_1")
                        .queryParam("from", "2025-09-01T06:00")
                        .queryParam("to", "2025-09-01T09:00")
                        .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<ProductionRun>>() {}).block();
        return linePerformance;
    }
}
