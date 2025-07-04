package com.chatgm.agents_server_mcp.interceptor;

import com.chatgm.agents_server_mcp.contex.SessionContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(HeaderInterceptor.class);
    
    @Autowired
    private SessionContext sessionContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String instance = request.getHeader("instance");
        String number = request.getHeader("number");
        String requestURI = request.getRequestURI();
        
        // Se algum header está presente, atualiza a sessão
        if (instance != null || number != null) {
            sessionContext.setCurrentSession(instance, number);
            logger.info("Headers capturados - Instance: {}, Number: {} para URI: {}", 
                       instance, number, requestURI);
        }
        
        // Para requisições MCP sem headers, os headers da sessão ativa serão usados
        if (requestURI.contains("/mcp/message") && !sessionContext.hasActiveSession()) {
            logger.warn("Requisição MCP sem sessão ativa");
        }
        
        return true;
    }
}
