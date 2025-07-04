package com.chatgm.agents_server_mcp;

import com.chatgm.agents_server_mcp.interceptor.HeaderInterceptor;
import com.chatgm.agents_server_mcp.tools.MidiaTool;
import com.chatgm.agents_server_mcp.tools.NotificationTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AgentsServerMcpApplication implements WebMvcConfigurer {

    @Autowired
    private HeaderInterceptor headerInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(AgentsServerMcpApplication.class, args);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor)
                .addPathPatterns("/api/v1/sse", "/mcp/message");
    }
    
    @Bean
    public ToolCallbackProvider toolCallbackProvider(NotificationTool notificationTool, MidiaTool midiaTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(notificationTool, midiaTool)
                .build();
    }

    @Bean
    ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
        return new DefaultToolExecutionExceptionProcessor(true);
    }
}
