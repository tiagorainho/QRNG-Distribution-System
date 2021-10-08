package qrng.PrimeService.Generator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import qrng.PrimeService.RN.RN;

@Document
public class Generator {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String url;
    private GeneratorType type;
    private List<RN> randomNumbers;
    private long cache_size;

    // POSSIBLE CHANGE THE RANDOM NUMBERS TO THE RNREPOSITORY WITH A IN MEMORY QUEUE

    public Generator(String name, String url, GeneratorType type, long cache_size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.randomNumbers = new ArrayList<>();
        this.cache_size = cache_size;
    }

    public Generator() {
        this.randomNumbers = new ArrayList<>();
        this.cache_size = 100;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public GeneratorType getType() {
        return this.type;
    }

    public void addRN(RN number) {
        this.randomNumbers.add(number);
    }

    public void addRN(List<Long> numbers) {
        this.randomNumbers.addAll(numbers.stream().map(n -> new RN(new BigInteger(String.valueOf(n)))).collect(Collectors.toList()));
    }

    public List<RN> getRandomNumbers() {
        return this.randomNumbers;
    }

    public long getCacheSize() {
        return this.cache_size;
    }

}
