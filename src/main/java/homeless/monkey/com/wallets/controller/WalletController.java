package homeless.monkey.com.wallets.controller;

import homeless.monkey.com.wallets.dto.BalanceOperationRequestDto;
import homeless.monkey.com.wallets.dto.BalanceResponseDto;
import homeless.monkey.com.wallets.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<BalanceResponseDto> operateBalance(@Valid @RequestBody BalanceOperationRequestDto dto){
        return ResponseEntity.ok(walletService.operateBalance(dto));
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID id){
        return ResponseEntity.ok(walletService.getBalance(id));
    }
}
