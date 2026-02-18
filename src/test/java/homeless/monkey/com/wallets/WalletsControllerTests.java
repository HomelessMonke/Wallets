package homeless.monkey.com.wallets;

import homeless.monkey.com.wallets.entity.OperationType;
import homeless.monkey.com.wallets.entity.WalletEntity;
import homeless.monkey.com.wallets.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("Интеграционные тесты WalletController")
class WalletsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("wallets_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.enabled", () -> "true");
    }

    @BeforeEach
    void reset(){
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное зачисление баланса")
    void shouldSuccessfullyDeposit() throws Exception {
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new WalletEntity(walletId, BigDecimal.ZERO));
        String requestBody = getBalanceRequestDtoJson(walletId, OperationType.DEPOSIT, new BigDecimal(1500.50));

        mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.50));
    }

    @Test
    @DisplayName("Успешное снятие баланса")
    void shouldSuccessfullyWithdraw() throws Exception {
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new WalletEntity(walletId, new BigDecimal(1000)));
        String requestBody = getBalanceRequestDtoJson(walletId, OperationType.WITHDRAW, new BigDecimal(500.50));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(499.50));
    }

    @Test
    @DisplayName("Не достаточно баланса")
    void shouldReturnNotEnoughBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new WalletEntity(walletId, BigDecimal.ZERO));
        String requestBody = getBalanceRequestDtoJson(walletId, OperationType.WITHDRAW, new BigDecimal(500));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Недостаточно средств"));
    }

    @Test
    @DisplayName("Кошелек отсутствует в БД")
    void shouldReturnWalletNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        String requestBody = getBalanceRequestDtoJson(nonExistentId, OperationType.WITHDRAW, new BigDecimal(500));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Карта с ID:%s не найдена".formatted(nonExistentId)));
    }

    @Test
    @DisplayName("Успешно получен баланс")
    void shouldReturnBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        walletRepository.save(new WalletEntity(walletId, new BigDecimal(500)));

        mockMvc.perform(get("/api/v1/wallets/{id}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("500.00"));
    }

    String getBalanceRequestDtoJson(UUID id, OperationType type, BigDecimal amount){
        return """
                {
                    "walletId": "%s",
                    "operationType": "%s",
                    "amount": %s
                }
                """.formatted(id, type.toString(), amount.toString());
    }
}
