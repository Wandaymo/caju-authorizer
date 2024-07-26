package br.com.wandaymo.caju.authorizer.service;

import br.com.wandaymo.caju.authorizer.api.dto.TransactionDTO;
import br.com.wandaymo.caju.authorizer.domain.Account;
import br.com.wandaymo.caju.authorizer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
class AccountService {

    private static final String FOOD = "FOOD";
    private static final String MEAL = "MEAL";
    private static final String CASH = "CASH";
    private static final String APPROVED = "00";
    private static final String REJECTED = "51";
    private static final String ERROR = "07";

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public String update(TransactionDTO transactionDTO) {
        Optional<Account> accountOptional = accountRepository.findById(transactionDTO.getAccountId());

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            String ammountDebit = verifyBalance(account, transactionDTO.getMcc(), transactionDTO.getTotalAmount(), transactionDTO.getMerchant());

            if (ammountDebit.equals(REJECTED)) {
                return REJECTED;
            } else if (classifyMcc(transactionDTO.getMerchant(), transactionDTO.getMcc()).equals(FOOD) && ammountDebit.equals(FOOD)) {
                account.setFood(account.getFood() - transactionDTO.getTotalAmount());
            } else if (classifyMcc(transactionDTO.getMerchant(), transactionDTO.getMcc()).equals(MEAL) && ammountDebit.equals(MEAL)) {
                account.setMeal(account.getMeal() - transactionDTO.getTotalAmount());
            } else {
                account.setCash(account.getCash() - transactionDTO.getTotalAmount());
            }

            accountRepository.save(account);

            return APPROVED;
        }

        return ERROR;
    }

    private String classifyMcc(String merchant, String mcc) {
        String[] mealPatterns = {
                "REST", "RESTA", "RESTAU", "RESTAUR", "RESTAURA", "RESTAURAN", "RESTAURANT", "RESTAURANTE", "PADAR",
                "PADARI", "PADARIA", "EATS"
        };

        String[] foodPatterns = {
                "MERC", "MERCA", "MERCAD", "MERCADO", "SUPERME", "SUPERMER", "SUPERMERC", "SUPERMERCA", "SUPERMERCAD",
                "SUPERMERCADO", "SUPER-MERCADO", "MERCE", "MERCEA", "MERCEAR", "MERCEARI", "MERCEARIA", "AÇOU", "AÇOUG",
                "AÇOUGU", "AÇOUGUE", "ACOU", "ACOUG", "ACOUGU", "ACOUGUE"
        };

        for (String pattern : mealPatterns) {
            if (merchant.toUpperCase().matches(".*\\b" + pattern + "\\b.*")) {
                return MEAL;
            }
        }

        for (String pattern : foodPatterns) {
            if (merchant.toUpperCase().matches(".*\\b" + pattern + "\\b.*")) {
                return FOOD;
            }
        }

        if (mcc.equals("5411") || mcc.equals("5412")) {
            return FOOD;
        } else if (mcc.equals("5811") || mcc.equals("5812")) {
            return MEAL;
        }

        return CASH;
    }

    private String verifyBalance(Account account, String mcc, Double totalAmount, String merchant) {
        if (totalAmount == null || totalAmount == 0) {
            return REJECTED;
        }
        else if (classifyMcc(merchant, mcc).equals(FOOD) && account.getFood() >= totalAmount) {
            return FOOD;
        } else if (classifyMcc(merchant, mcc).equals(MEAL) && account.getMeal() >= totalAmount) {
            return MEAL;
        } else if ((classifyMcc(merchant, mcc).equals(FOOD)
                || classifyMcc(merchant, mcc).equals(MEAL)
                || classifyMcc(merchant, mcc).equals(CASH))
                && account.getCash() >= totalAmount) {
            return CASH;
        } else {
            return REJECTED;
        }
    }
}
