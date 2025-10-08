package com.example.demo.feller.dto;

/**
 * DTO para receber resposta da IA Feller
 */
public class FellerResponseDTO {

    private String response;
    private Long timestamp;

    public FellerResponseDTO() {
    }

    public FellerResponseDTO(String response) {
        this.response = response;
        this.timestamp = System.currentTimeMillis();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
