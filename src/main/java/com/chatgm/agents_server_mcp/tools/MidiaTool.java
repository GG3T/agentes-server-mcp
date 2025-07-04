package com.chatgm.agents_server_mcp.tools;

import com.chatgm.agents_server_mcp.contex.SessionContext;
import com.chatgm.agents_server_mcp.service.EnviarMidiaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MidiaTool {
    
    private static final Logger logger = LoggerFactory.getLogger(MidiaTool.class);

    @Autowired
    EnviarMidiaService enviarMidiaService;

    @Autowired
    private SessionContext sessionContext;

    @Tool(description = "Tool responsavel por enviar video sobre clincia para o cliente e imagem valores sobre o clube botox. e video sobre a consulta ESTETICA\n" +
            "\n" +
            "Enum : CAPILAR,CLUBEBOTOX,ESTETICA.\n" +
            "\n" +
            "Sobre consulta capilar = CAPILAR\n" +
            "documento com informações de orçamento sobre clube botox = CLUBEBOTOX\n" +
            "Estetica ou plano de envelhecimento, enviar o video = ESTETICA")
    public String sendVideo(@ToolParam(description = "ENUM: VIDEO,CLUBEBOTOX,ESTETICA") String tipoDeEnvio) {
        
        logger.info("Executando MidiaTool - Tipo de envio: {}", tipoDeEnvio);
        
        // Obtém headers da sessão
        String instance = sessionContext.getCurrentInstance();
        String number = sessionContext.getCurrentNumber();
        
        logger.info("Sessão ativa: {}", sessionContext.getSessionInfo());
        
        // Valida se existe pelo menos um header
        if (!sessionContext.hasActiveSession()) {
            String errorMsg = "Headers 'instance' ou 'number' são obrigatórios para enviar vídeos";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        enviarMidiaService.sendMidia(tipoDeEnvio);
        
        String result = String.format("✅ Vídeo enviado com sucesso - Instance: %s, Number: %s, Tipo: %s", 
                                    instance != null ? instance : "N/A",
                                    number != null ? number : "N/A",
                                    tipoDeEnvio);
        
        logger.info("Resultado: {}", result);
        return result;
    }
}
