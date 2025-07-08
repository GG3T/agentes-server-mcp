package com.chatgm.agents_server_mcp.request;

public class SendMediaRequest {
        private String number;
        private String tipoDeEnvio;
        private String instancia;

        public SendMediaRequest() { }

        public SendMediaRequest(String number, String tipoDeEnvio, String instancia) {
            this.number = number;
            this.tipoDeEnvio = tipoDeEnvio;
            this.instancia = instancia;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTipoDeEnvio() {
            return tipoDeEnvio;
        }

        public void setTipoDeEnvio(String tipoDeEnvio) {
            this.tipoDeEnvio = tipoDeEnvio;
        }

        public String getInstancia() {
            return instancia;
        }

        public void setInstancia(String instancia) {
            this.instancia = instancia;
        }
    }