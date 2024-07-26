package br.com.wandaymo.caju.authorizer.service;

import br.com.wandaymo.caju.authorizer.api.dto.TransactionDTO;
import br.com.wandaymo.caju.authorizer.api.dto.TransactionResponseDTO;
import br.com.wandaymo.caju.authorizer.domain.Transaction;
import br.com.wandaymo.caju.authorizer.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionRepository transactionRepository;

    public TransactionResponseDTO purchase(TransactionDTO transactionDTO) {

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

        transactionResponseDTO.setCode(accountService.update(transactionDTO));

        save(transactionDTO, transactionResponseDTO.getCode());

        return transactionResponseDTO;
    }

    public void save(TransactionDTO transactionDTO, String status) {

        Transaction transaction = new Transaction();
        transaction.setAccountId(transactionDTO.getAccountId());
        transaction.setAmount(transactionDTO.getTotalAmount());
        transaction.setMcc(transactionDTO.getMcc());
        transaction.setMerchant(transactionDTO.getMerchant());
        transaction.setStatus(status);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}
