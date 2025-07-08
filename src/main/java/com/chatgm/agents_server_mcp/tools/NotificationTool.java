package com.chatgm.agents_server_mcp.tools;

import com.chatgm.agents_server_mcp.contex.SessionContext;
import com.chatgm.agents_server_mcp.service.NotificationService;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationTool {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationTool.class);
    
    @Autowired
    private SessionContext sessionContext;
    
    @Autowired
    private NotificationService notificationService;

    @Tool(description = "Envia notificação para a clínica com informações do atendimento ao paciente. para o tipoNotificacao enviar Enum: EQUIPE, AGENDAMENTO, RECORRENTE"
    )
    @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE)
    public String sendNotification(
            @ToolParam(description = "Resumo completo da conversa realizada com o paciente") String resumoConversa,
            @ToolParam(description = "Principal dúvida ou questionamento apresentado pelo paciente") String duvidaCliente,
            @ToolParam(description = "Nível de interesse do paciente no procedimento ou consulta (alto, médio, baixo)") String interesse,
            @ToolParam(description = "Valor estimado do procedimento ou consulta, ou 'A negociar' se não definido") String valor,
            @ToolParam(description = "Tipo de notificação a ser enviada (urgente, normal, informativa)") String tipoNotificacao,
            @ToolParam(description = "Tipo de consulta true para convenio e false para particular)") boolean ehConvenio)
            {

        logger.info("Executando sendNotification - Sessão ativa: {}", sessionContext.getSessionInfo());
        
        // Valida se existe sessão ativa
        if (!sessionContext.hasActiveSession()) {
            String errorMsg = "Headers 'instance' ou 'number' são obrigatórios para enviar notificações";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        // Chama o serviço para enviar a notificação
        return notificationService.enviarNotificacao(tipoNotificacao, resumoConversa, duvidaCliente, interesse, valor);
    }
}
