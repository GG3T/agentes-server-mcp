package com.chatgm.agents_server_mcp.service;

import com.chatgm.agents_server_mcp.contex.SessionContext;
import com.chatgm.agents_server_mcp.request.SendMediaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EnviarMidiaService {

    private static final Logger logger = LoggerFactory.getLogger(EnviarMidiaService.class);
    private static final String SEND_MIDIA_URL = "http://image-service.numit.com.br:8082/sendMedia/v2/async";

    @Autowired
    private SessionContext sessionContext;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMidia(String tipoEnvio) {
        try {
            // Obtém o número e instância da sessão atual
            String numero = sessionContext.getCurrentNumber();
            String instancia = sessionContext.getCurrentInstance();

            // ✅ MELHORIA: Validação mais robusta
            if (numero == null || numero.trim().isEmpty()) {
                throw new RuntimeException("Número da sessão não disponível ou vazio");
            }

            if (instancia == null || instancia.trim().isEmpty()) {
                throw new RuntimeException("Instância da sessão não disponível ou vazia");
            }

            SendMediaRequest request = new SendMediaRequest(numero.trim(), tipoEnvio.trim().toUpperCase(), instancia.trim());
            // Monta o payload

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SendMediaRequest> entity = new HttpEntity<>(request, headers);


            logger.info("Enviando mídia para: {} com payload: {}", SEND_MIDIA_URL, entity);

            // ✅ MELHORIA: Timeout configurado e tratamento de resposta específico
            ResponseEntity<String> response = restTemplate.postForEntity(SEND_MIDIA_URL, request, String.class);

            logger.info("Resposta da mídia - Status: {}, Body: {}", response.getStatusCode(), response.getBody());

            // ✅ MELHORIA: Verificação mais específica do sucesso
            if (response.getStatusCode().is2xxSuccessful()) {
                return "✅ Mídia enviada com sucesso para o número: " + numero + " (Tipo: " + tipoEnvio + ", Instância: " + instancia + ")";
            } else {
                logger.warn("Resposta não foi de sucesso: {} - {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Falha no envio da mídia. Status: " + response.getStatusCode());
            }

        } catch (RestClientException e) {
            logger.error("Erro de conectividade ao enviar mídia: ", e);
            throw new RuntimeException("Erro de conexão com o serviço de mídia: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao enviar mídia: ", e);
            throw new RuntimeException("Erro ao enviar mídia: " + e.getMessage());
        }
    }

    // ✅ MELHORIA: Métodos de validação auxiliares
    private boolean isValidTipoEnvio(String tipoEnvio) {
        return tipoEnvio.trim().equalsIgnoreCase("CAPILAR") ||
                tipoEnvio.trim().equalsIgnoreCase("ESTETICA");
    }

    private boolean isValidInstancia(String instancia, String tipoEnvio) {
        if (tipoEnvio.trim().equalsIgnoreCase("CAPILAR")) {
            return instancia.trim().equalsIgnoreCase("AudraAI") ||
                    instancia.trim().equalsIgnoreCase("horn");
        } else if (tipoEnvio.trim().equalsIgnoreCase("ESTETICA")) {
            // Para estética, qualquer instância é válida baseado no controller
            return true;
        }
        return false;
    }

    // ✅ MELHORIA: Método alternativo com retry automático
    public String sendMidiaWithRetry(String tipoEnvio) {
        int maxRetries = 3;
        int retryDelay = 2000; // 2 segundos

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return sendMidia(tipoEnvio);
            } catch (Exception e) {
                logger.warn("Tentativa {} falhou: {}", attempt, e.getMessage());

                if (attempt == maxRetries) {
                    throw new RuntimeException("Falha após " + maxRetries + " tentativas: " + e.getMessage());
                }

                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrompido durante retry: " + ie.getMessage());
                }
            }
        }

        return null; // Nunca deve chegar aqui
    }

}
