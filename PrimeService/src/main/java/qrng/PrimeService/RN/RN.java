package qrng.PrimeService.RN;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class RN {
    private BigInteger value;
    private LocalDateTime createdAt;

    public RN(BigInteger value) {
        this.value = value;
        this.createdAt = LocalDateTime.now();
    }

    public BigInteger getValue() {
        return this.value;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
