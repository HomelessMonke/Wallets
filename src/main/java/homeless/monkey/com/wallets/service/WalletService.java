package homeless.monkey.com.wallets.service;

import homeless.monkey.com.wallets.dto.BalanceOperationRequestDto;
import homeless.monkey.com.wallets.dto.BalanceResponseDto;
import homeless.monkey.com.wallets.entity.WalletEntity;
import homeless.monkey.com.wallets.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public BalanceResponseDto operateBalance(BalanceOperationRequestDto dto){

        UUID walletId = dto.walletId();
        WalletEntity wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Карта с ID:" + walletId + " не найдена"));

        if(dto.isDeposit())
            wallet.addBalance(dto.amount());
        else {
            if(!wallet.enoughBalance(dto.amount()))
                throw new IllegalStateException("Недостаточно средств");

            wallet.subtractBalance(dto.amount());
        }

        walletRepository.save(wallet);
        return new BalanceResponseDto(walletId, wallet.getBalance());
    }

    public BigDecimal getBalance(UUID walletId){
        WalletEntity wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Карта с ID:" + walletId + " не найдена"));

        return wallet.getBalance();
    }
}
