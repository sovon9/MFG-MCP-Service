package com.sovon9.MCPServer.controller;

import com.sovon9.MCPServer.dto.MachineData;
import com.sovon9.MCPServer.dto.ProductionRun;
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
    public MachineData getLinePerformance()
    {
        MachineData machineData = webClient.get().uri(uriBuilder -> uriBuilder.path("/machine/" + "MC001")
//                        .queryParam("from", "2025-09-01T06:00")
//                        .queryParam("to", "2025-09-01T09:00")
                        .build())
                .retrieve().bodyToMono(MachineData.class).block();
        return machineData;
    }
}
