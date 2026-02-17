package homeless.monkey.com.wallets.controller;

import homeless.monkey.com.wallets.dto.BalanceOperationRequestDto;
import homeless.monkey.com.wallets.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<?> operateBalance(@Valid @RequestBody BalanceOperationRequestDto dto){
        return ResponseEntity.ok(walletService.operateBalance(dto));
    }
}
