package com.example.digitalwallet.dto;
import lombok.Data;
@Data
public class TransferRequest {
    private String fromEmail;
    private String toEmail;
    private Double amount;
    
}
