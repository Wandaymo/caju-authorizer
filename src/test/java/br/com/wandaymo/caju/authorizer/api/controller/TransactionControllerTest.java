package br.com.wandaymo.caju.authorizer.api.controller;

import br.com.wandaymo.caju.authorizer.api.dto.TransactionDTO;
import br.com.wandaymo.caju.authorizer.api.dto.TransactionResponseDTO;
import br.com.wandaymo.caju.authorizer.service.TransactionService;
import br.com.wandaymo.caju.authorizer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testPurchase() throws Exception {
        String token = createNewUser("userOne", "passOne");

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(1L);
        transactionDTO.setTotalAmount(100.0);
        transactionDTO.setMcc("5411");
        transactionDTO.setMerchant("Mercado");

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setCode("00");

        when(transactionService.purchase(any(TransactionDTO.class))).thenReturn(transactionResponseDTO);

        mockMvc.perform(post("/v1/purchase")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionResponseDTO)));
    }

    @Test
    void testPurchaseAccountNotFound() throws Exception {
        String token = createNewUser("userTwo", "passTwo");

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(15L);
        transactionDTO.setTotalAmount(100.0);
        transactionDTO.setMcc("5411");
        transactionDTO.setMerchant("Mercado");

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setCode("07");

        when(transactionService.purchase(any(TransactionDTO.class))).thenReturn(transactionResponseDTO);

        mockMvc.perform(post("/v1/purchase")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionResponseDTO)));
    }

    @Test
    void testPurchaseInsufficientBalance() throws Exception {
        String token = createNewUser("userThree", "passThree");

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(1L);
        transactionDTO.setTotalAmount(1000.0);
        transactionDTO.setMcc("5411");
        transactionDTO.setMerchant("Mercado");

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setCode("51");

        when(transactionService.purchase(any(TransactionDTO.class))).thenReturn(transactionResponseDTO);

        mockMvc.perform(post("/v1/purchase")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionResponseDTO)));
    }

    private String createNewUser(String user, String pass) throws Exception {
        String body = "{\"username\": \"" + user + "\", \"password\": \"" + pass + "\"}";

        return mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
