package qrng.PrimeService.RN;

import java.util.List;

import qrng.PrimeService.Generator.Generator;

public interface RNClientInterface {
    List<Long> requestRandomNumbers(Generator generator, long n);

    Generator getGeneratorByName(String name);
}
