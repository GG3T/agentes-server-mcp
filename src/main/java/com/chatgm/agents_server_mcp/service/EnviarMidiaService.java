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
public class EnviarMidiaService {

    private static final Logger logger = LoggerFactory.getLogger(EnviarMidiaService.class);
    private static final String SEND_MIDIA_URL = "http://image-service.numit.com.br:8082/v2/sendMedia";

    @Autowired
    private SessionContext sessionContext;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMidia(String tipoEnvio) {
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

            // Monta o payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("numero", numero);
            payload.put("tipoDeEnvio", tipoEnvio);
            payload.put("instancia", instancia);


            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            logger.info("Enviando midia para: {} com payload: {}", SEND_MIDIA_URL, payload);

            // Faz a requisição
            ResponseEntity<String> response = restTemplate.postForEntity(SEND_MIDIA_URL, request, String.class);

            logger.info("Resposta da notificação - Status: {}, Body: {}", response.getStatusCode(), response.getBody());

            return "✅ Notificação enviada com sucesso para o número: " + numero;


        } catch (Exception e) {
            logger.error("Erro ao enviar notificação: ", e);
            throw new RuntimeException("Erro ao enviar notificação: " + e.getMessage());
        }
    }




}
