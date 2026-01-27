package com.sovon9.MCPServer.tool;

import com.sovon9.MCPServer.dto.DowntimeEvent;
import com.sovon9.MCPServer.dto.ProductionRun;
import com.sovon9.MCPServer.dto.QualityEvent;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MCPTools {

    public WebClient webClient;

    public MCPTools() {
        webClient = WebClient.builder().
                baseUrl("http://localhost:8081/api").build();
    }

    @McpTool(name = "getLinePerformance", description = "Returns production performance metrics for a manufacturing line over a specific time window,\n" +
            "including planned vs actual output, downtime, quality losses, and overall equipment effectiveness (OEE).\n" +
            "Use this to explain why a line underperformed or to summarize how well it ran")
    public List<ProductionRun> getLinePerformance(@McpToolParam(description = "Production line ID, e.g. LINE_1") String lineId,
                                                  @McpToolParam(description = "Start of time window in 2025-09-01T06:00:00 format", required = false) String from,
                                                  @McpToolParam(description = "End of time window in 2025-09-01T08:00:00 format", required = false) String to)
    {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/production/run/line/" + lineId)
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<ProductionRun>>() {}).block();
    }

    @McpTool(name = "getRunDowntime", description = "Returns all downtime events that occurred during a specific production run.\n" +
            "  Use this to explain why production stopped or why output was lower than expected.\n" +
            "  Each event includes the machine, reason, and duration")
    public List<DowntimeEvent> getRunDowntime(@McpToolParam(description = "Production run ID, e.g. RUN_1009") String runId)
    {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/production/run/" + runId+"/downtime")
                        .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<DowntimeEvent>>() {}).block();
    }

    @McpTool(name = "getRunQuality", description = "Returns quality and scrap information for a production run,\n" +
            "  including rejected quantity and defect types.\n" +
            "  Use this to explain production losses caused by quality problems")
    public List<QualityEvent> getRunQuality(@McpToolParam(description = "Production run ID, e.g. RUN_1009") String runId)
    {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/production/run/" + runId+"/quality")
                        .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<QualityEvent>>() {}).block();
    }

    @McpTool(name = "getMachineInfo", description = "Returns the human-readable name and type of a machine.\n" +
            "  Use this when explaining downtime or defects so responses use real machine names instead of IDs")
    public List<QualityEvent> getMachineInfo(@McpToolParam(description = "Machine ID, e.g. MC001") String machineId)
    {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/machine/" + machineId)
                        .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<QualityEvent>>() {}).block();
    }

}
