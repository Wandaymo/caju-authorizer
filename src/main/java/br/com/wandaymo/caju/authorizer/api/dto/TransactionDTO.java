package br.com.wandaymo.caju.authorizer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long accountId;
    private Double totalAmount;
    private String mcc;
    private String merchant;

}
