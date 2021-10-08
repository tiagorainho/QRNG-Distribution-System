package qrng.PrimeService.RN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RNRepository {        // NAO ESTA A SER USADO !!!!
    private List<RN> randomNumbers;

    public RNRepository() {
        this.randomNumbers = new ArrayList<>();
    }

    public void addRN(RN number) {
        this.randomNumbers.add(number);
    }

    public List<RN> getRandomNumbersPrivate(int n) {
        return Stream.generate(
            () -> randomNumbers.remove(0)
        )
        .limit(n)
        .collect(Collectors.toList());
    }

    public RN getRandomNumber() {
        return randomNumbers.remove(0);
    }
    
}
