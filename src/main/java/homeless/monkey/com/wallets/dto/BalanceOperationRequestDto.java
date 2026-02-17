package homeless.monkey.com.wallets.dto;

import homeless.monkey.com.wallets.entity.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record BalanceOperationRequestDto (

        @NotNull(message = "walletId не может быть пустым")
        UUID walletId,

        @NotNull(message = "operationType не может быть пустым")
        OperationType operationType,

        @NotNull(message = "amount не может быть пустым")
        @Positive(message = "amount должен быть больше 0")
        BigDecimal amount
){
        public boolean isDeposit(){
                return operationType == OperationType.DEPOSIT; }
}
