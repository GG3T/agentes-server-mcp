package com.chatgm.agents_server_mcp.service;

import com.chatgm.agents_server_mcp.contex.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String NOTIFICATION_URL = "http://178.156.142.177:5151/api/v1/notifications/send";

    @Autowired
    private SessionContext sessionContext;

    private final RestTemplate restTemplate = new RestTemplate();

    public String enviarNotificacao(String tipoNotificacao, String resumoConversa, String duvidaCliente, String interesse, String valor) {
        try {
            // Obtém o número da sessão atual
            String numero = sessionContext.getCurrentNumber();
            String instancia = sessionContext.getCurrentInstance();

            if (numero == null) {
                throw new RuntimeException("Número da sessão não disponível");
            }

            if (instancia == null) {
                throw new RuntimeException("instancia da sessão não disponível");
            }

// 1) Cria o map principal
            Map<String, Object> payload = new HashMap<>();
            payload.put("numero", numero);
            payload.put("instancia", instancia);
            payload.put("tipoNotificacao", tipoNotificacao);

// 2) Cria o map interno "dados"
            Map<String, Object> dados = new HashMap<>();
            dados.put("interesse", interesse);
            dados.put("resumoConversa", resumoConversa);
            dados.put("valor", valor);
            dados.put("duvidaCliente", duvidaCliente);
// se tiver outros campos, ex:
// dados.put("origemLead", origemLead);

// 3) Insere o map "dados" no payload
            payload.put("dados", dados);
            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            logger.info("Enviando notificação para: {} com payload: {}", NOTIFICATION_URL, payload);

            // Faz a requisição
            ResponseEntity<String> response = restTemplate.postForEntity(NOTIFICATION_URL, request, String.class);

            logger.info("Resposta da notificação - Status: {}, Body: {}", response.getStatusCode(), response.getBody());

            return "✅ Notificação enviada com sucesso para o número: " + numero;

        } catch (Exception e) {
            logger.error("Erro ao enviar notificação: ", e);
            throw new RuntimeException("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}
