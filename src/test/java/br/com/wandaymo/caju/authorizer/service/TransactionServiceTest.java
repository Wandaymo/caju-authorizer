package br.com.wandaymo.caju.authorizer.service;

import br.com.wandaymo.caju.authorizer.api.dto.TransactionDTO;
import br.com.wandaymo.caju.authorizer.api.dto.TransactionResponseDTO;
import br.com.wandaymo.caju.authorizer.domain.Transaction;
import br.com.wandaymo.caju.authorizer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionDTO transactionDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(1L);
        transactionDTO.setTotalAmount(100.0);
        transactionDTO.setMcc("5411");
        transactionDTO.setMerchant("Mercado");
    }

    @Test
    public void testPurchase() {
        when(accountService.update(any(TransactionDTO.class))).thenReturn("00");

        TransactionResponseDTO responseDTO = transactionService.purchase(transactionDTO);

        assertNotNull(responseDTO);
        assertEquals("00", responseDTO.getCode());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testPurchaseWithAccountNotFound() {
        when(accountService.update(any(TransactionDTO.class))).thenReturn("07");

        TransactionResponseDTO responseDTO = transactionService.purchase(transactionDTO);

        assertNotNull(responseDTO);
        assertEquals("07", responseDTO.getCode());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testPurchaseWithInsufficientBalance() {
        when(accountService.update(any(TransactionDTO.class))).thenReturn("51");

        TransactionResponseDTO responseDTO = transactionService.purchase(transactionDTO);

        assertNotNull(responseDTO);
        assertEquals("51", responseDTO.getCode());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testSave() {
        transactionService.save(transactionDTO, "00");

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(captor.capture());
        Transaction savedTransaction = captor.getValue();
        assertNotNull(savedTransaction);
        assertEquals(transactionDTO.getAccountId(), savedTransaction.getAccountId());
        assertEquals(transactionDTO.getTotalAmount(), savedTransaction.getAmount());
        assertEquals(transactionDTO.getMcc(), savedTransaction.getMcc());
        assertEquals(transactionDTO.getMerchant(), savedTransaction.getMerchant());
        assertEquals("00", savedTransaction.getStatus());
        assertNotNull(savedTransaction.getTransactionDate());
    }
}
