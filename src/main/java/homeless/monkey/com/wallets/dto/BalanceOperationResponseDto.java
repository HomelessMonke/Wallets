package homeless.monkey.com.wallets.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceOperationResponseDto (
        UUID id,
        BigDecimal balance
){}
