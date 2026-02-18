package homeless.monkey.com.wallets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public boolean enoughBalance(BigDecimal amount) {
        return balance.compareTo(amount)>=0; }

    public void addBalance(BigDecimal addBalance){ balance = balance.add(addBalance); }

    public void subtractBalance(BigDecimal subBalance){

        if(!enoughBalance(subBalance))
            throw new IllegalArgumentException("На карте не достаточно средств");

        balance = balance.subtract(subBalance);
    }
}
