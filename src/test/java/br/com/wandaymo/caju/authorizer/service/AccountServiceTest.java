package br.com.wandaymo.caju.authorizer.service;

import br.com.wandaymo.caju.authorizer.api.dto.TransactionDTO;
import br.com.wandaymo.caju.authorizer.domain.Account;
import br.com.wandaymo.caju.authorizer.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        String result = accountService.update(new TransactionDTO(1L, 100.0, "5411", "Mercado"));
        assertEquals("07", result);
    }

    @Test
    void testUpdateInsufficientFoodBalanceButSufficientCash() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(50.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5411", "Mercado"));
        assertEquals("00", result);
    }

    @Test
    void testUpdateInsufficientFoodBalanceAndInsufficientCash() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(50.0);
        account.setMeal(100.0);
        account.setCash(30.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5411", "Mercado"));
        assertEquals("51", result);
    }

    @Test
    void testUpdateSufficientFoodBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5411", "Mercado"));
        assertEquals("00", result);
        assertEquals(100.0, account.getFood());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateExactlyFoodBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 200.0, "5411", "Mercado"));
        assertEquals("00", result);
        assertEquals(0.0, account.getFood());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientMealBalanceUberEatsMerchant() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(
                1L, 50.0, "7777", "UBER EATS                   SAO PAULO BR"
        ));
        assertEquals("00", result);
        assertEquals(50.0, account.getMeal());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientFoodBalanceAndWrongMcc() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "9999", "Mercado"));
        assertEquals("00", result);
        assertEquals(100.0, account.getFood());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientFoodBalanceAndWrongMerchant() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5411", "Hjdfieijkje"));
        assertEquals("00", result);
        assertEquals(100.0, account.getFood());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateInsufficientMealBalanceButSufficientCash() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(50.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5812", "Restaurante"));
        assertEquals("00", result);
    }

    @Test
    void testUpdateInsufficientMealBalanceAndInsufficientCash() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(50.0);
        account.setCash(40.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5812", "Rest"));
        assertEquals("51", result);
    }

    @Test
    void testUpdateWhenAmountIsZero() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(50.0);
        account.setCash(40.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 0.0, "5812", "Rest"));
        assertEquals("51", result);
    }

    @Test
    void testUpdateSufficientMealBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(200.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5812", "Restau"));
        assertEquals("00", result);
        assertEquals(100.0, account.getMeal());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientMealBalanceAndWrongMcc() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(200.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "8888", "Restau"));
        assertEquals("00", result);
        assertEquals(100.0, account.getMeal());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientMealBalanceAndWrongMerchant() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(200.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "5811", "Ojksyqtrzx"));
        assertEquals("00", result);
        assertEquals(100.0, account.getMeal());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateInsufficientCashBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(50.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "1234", "Trip"));
        assertEquals("51", result);
    }

    @Test
    void testUpdateSufficientCashBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(1L, 100.0, "1234", "Hotel"));
        assertEquals("00", result);
        assertEquals(100.0, account.getCash());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientCashBalancePagMerchant() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(
                1L, 100.0, "1234", "PAG*JoseDaSilva          RIO DE JANEI BR"
        ));
        assertEquals("00", result);
        assertEquals(100.0, account.getCash());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientCashBalancePicPayMerchant() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(
                1L, 100.0, "1234", "PICPAY*BILHETEUNICO           GOIANIA BR"
        ));
        assertEquals("00", result);
        assertEquals(100.0, account.getCash());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateSufficientCashBalanceUberMerchantTrip() {
        Account account = new Account();
        account.setId(1L);
        account.setFood(200.0);
        account.setMeal(100.0);
        account.setCash(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        String result = accountService.update(new TransactionDTO(
                1L, 100.0, "1234", "UBER TRIP                   SAO PAULO BR"
        ));
        assertEquals("00", result);
        assertEquals(100.0, account.getCash());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
