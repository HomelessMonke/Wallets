package homeless.monkey.com.wallets.service;

import homeless.monkey.com.wallets.dto.BalanceOperationRequestDto;
import homeless.monkey.com.wallets.dto.BalanceOperationResponseDto;
import homeless.monkey.com.wallets.entity.WalletEntity;
import homeless.monkey.com.wallets.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public BalanceOperationResponseDto operateBalance(BalanceOperationRequestDto dto){

        UUID walletID = dto.walletId();
        WalletEntity wallet = walletRepository.findByIdForUpdate(walletID)
                .orElseThrow(() -> new IllegalArgumentException("Карта с ID:" + walletID + " не найдена"));

        if(dto.isDeposit())
            wallet.addBalance(dto.amount());
        else {
            if(!wallet.enoughBalance(dto.amount()))
                throw new IllegalStateException("Недостаточно средств");

            wallet.subtractBalance(dto.amount());
        }

        walletRepository.save(wallet);
        return new BalanceOperationResponseDto(walletID, wallet.getBalance());
    }
}
