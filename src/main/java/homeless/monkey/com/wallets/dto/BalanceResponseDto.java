package homeless.monkey.com.wallets.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponseDto(
        UUID id,
        BigDecimal balance
){}
