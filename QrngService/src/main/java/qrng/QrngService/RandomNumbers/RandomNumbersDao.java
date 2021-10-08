package qrng.QrngService.RandomNumbers;

import java.math.BigInteger;
import java.util.List;

public interface RandomNumbersDao {
    
    void add(String generator, List<Byte> bytes);

    default void add(String generator) {
        add(generator, List.of());
    }

    BigInteger pop(String generator, int bitsToRetrieve);

    int size(String generator);

}
