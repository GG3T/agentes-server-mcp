package com.chatgm.agents_server_mcp.contex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Contexto global para gerenciar headers de sessão MCP
 */
@Component
public class SessionContext {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
    
    // Headers da sessão atual (última conexão ativa)
    private volatile String currentInstance;
    private volatile String currentNumber;
    
    /**
     * Armazena os headers da sessão atual
     */
    public void setCurrentSession(String instance, String number) {
        this.currentInstance = instance;
        this.currentNumber = number;
        logger.info("Sessão atualizada - Instance: {}, Number: {}", instance, number);
    }
    
    /**
     * Obtém o valor do header Instance
     */
    public String getCurrentInstance() {
        return currentInstance;
    }
    
    /**
     * Obtém o valor do header Number
     */
    public String getCurrentNumber() {
        return currentNumber;
    }
    
    /**
     * Verifica se existem headers ativos
     */
    public boolean hasActiveSession() {
        return currentInstance != null || currentNumber != null;
    }
    
    /**
     * Limpa a sessão atual
     */
    public void clearSession() {
        currentInstance = null;
        currentNumber = null;
        logger.info("Sessão limpa");
    }
    
    /**
     * Retorna informações da sessão atual formatadas
     */
    public String getSessionInfo() {
        return String.format("Instance: %s, Number: %s", 
                           currentInstance != null ? currentInstance : "null",
                           currentNumber != null ? currentNumber : "null");
    }
}
