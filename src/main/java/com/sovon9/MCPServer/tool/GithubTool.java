package com.sovon9.MCPServer.tool;

import com.sovon9.MCPServer.LinePerformance;
import com.sovon9.MCPServer.ProductionRun;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class GithubTool {

    public WebClient webClient;

    public GithubTool()
    {
        webClient = WebClient.builder().
                baseUrl("http://localhost:8081/api").build();
    }
    //tools
    @McpTool(name = "sovon-github", description = "return sovon github link")
    public String sovonGithubLink()
    {
        String link = """
                https://github.com/sovon9/MCP
                """;
        return link;
    }

    @McpTool(name = "getLinePerformance", description = "Returns production performance metrics for a manufacturing line over a specific time window,\n" +
            "including planned vs actual output, downtime, quality losses, and overall equipment effectiveness (OEE).\n" +
            "Use this to explain why a line underperformed or to summarize how well it ran")
    public List<ProductionRun> getLinePerformance(@McpToolParam(description = "Production line ID, e.g. LINE_1") String lineId,
                                                  @McpToolParam(description = "Start of time window in 2025-09-01T06:00:00 format", required = false) String from,
                                                  @McpToolParam(description = "End of time window in 2025-09-01T08:00:00 format", required = false) String to)
    {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/production/line/" + lineId)
                        .queryParam("from", from)
                        .queryParam("to", to)
            .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<ProductionRun>>() {}).block();
    }

}
