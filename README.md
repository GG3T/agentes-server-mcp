# Agents Server MCP

Servidor MCP (Model Context Protocol) limpo e funcional para integração com assistentes de IA.

## 📁 Estrutura do Projeto

```
src/main/java/com/chatgm/agents_server_mcp/
├── AgentsServerMcpApplication.java    # Classe principal Spring Boot
├── SessionContext.java                # Gerenciamento de sessão e headers
├── HeaderInterceptor.java             # Captura headers HTTP
├── NotificationTool.java              # Ferramenta: envio de notificações
└── MidiaTool.java                     # Ferramenta: envio de vídeos
```

## 🔧 Configuração

### Headers Obrigatórios
O servidor captura e utiliza os seguintes headers HTTP:

- **`instance`**: Identificador da instância do cliente
- **`number`**: Número identificador do cliente

### Conexão SSE
```javascript
{
  "url": "https://seu-dominio.com/api/v1/sse",
  "headers": {
    "instance": "clinica-centro",
    "number": "12345"
  }
}
```

## 🛠️ Ferramentas Disponíveis

### 1. NotificationTool
**Descrição**: Envia notificação para a clínica  
**Método**: `sendNotification(String mensagem)`  
**Retorna**: Confirmação de envio com informações da sessão

### 2. MidiaTool  
**Descrição**: Envia vídeo da clínica  
**Método**: `sendVideo(String tipoDeEnvio)`  
**Retorna**: Confirmação de envio com informações da sessão

## 🚀 Como Usar

1. **Inicie o servidor**:
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Conecte via SSE** com headers `instance` e `number`

3. **Execute ferramentas** através do protocolo MCP

## 📋 Logs

O sistema registra:
- Captura de headers na conexão inicial
- Execução de ferramentas com informações da sessão
- Erros quando headers obrigatórios não estão presentes

## ⚠️ Validações

- Headers `instance` ou `number` são obrigatórios
- Ferramentas falham se não houver sessão ativa
- Logs detalhados para debugging

## 🎯 Endpoints

- **SSE**: `/api/v1/sse` (conexão principal)
- **MCP**: `/mcp/message` (mensagens do protocolo)
- **Porta**: `8080`
