package com.chatgm.agents_server_mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class McpWebMvcConfig {

    private static final Logger log = LoggerFactory.getLogger(McpWebMvcConfig.class);

    // 1) Cria o transport WebMVC SSE em /sse + message endpoint em /mcp/message
    @Bean
    public WebMvcSseServerTransportProvider webMvcSseServerTransportProvider(ObjectMapper mapper) {
        // o endpoint SSE padrão é /sse, e a mensagem em /mcp/message
        return new WebMvcSseServerTransportProvider(mapper, "/mcp/message");
    }

    // 2) Registra o MCP Sync Server como bean, já configurado com capacidades e ferramentas
    @Bean(destroyMethod = "close")
    public McpSyncServer mcpSyncServer(WebMvcSseServerTransportProvider transportProvider) {
        // constrói o servidor
        McpSyncServer server = McpServer
            .sync(transportProvider)
            .serverInfo("mcp-server", "1.0.0")
            .capabilities(McpSchema.ServerCapabilities.builder()
                .tools(true)      // habilita tools
                .logging()        // habilita logging
                .build())
            .build();

        // 3) Define a tool sendNotification
        String schema = """
            {
              "type":"object",
              "properties":{
                "nome":{"type":"string"},
                "numero":{"type":"string"},
                "resumoConversa":{"type":"string"},
                "tipoNotificao":{"type":"string"},
                "duvidaCliente":{"type":"string"},
                "interesse":{"type":"string"},
                "valor":{"type":"string"}
              },
              "required":["nome","numero"]
            }
            """;

        var sendNotificationSpec = new McpServerFeatures
            .SyncToolSpecification(
                new McpSchema.Tool("sendNotification",
                         "Simula notificação e loga request",
                         schema),
                (exchange, arguments) -> {
                    log.info("=== sendNotification called ===");
                    arguments.forEach((k,v) -> log.info("{}: {}", k, v));
                    return new McpSchema.CallToolResult("Notificação enviada com sucesso.", false);
                }
            );

        // 4) Registra a tool no servidor
        server.addTool(sendNotificationSpec);
        log.info("MCP Server iniciado e sendNotification registrado");
        return server;
    }
}