package qrng.PrimeService.Prime;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import qrng.PrimeService.Generator.Generator;
import qrng.PrimeService.Generator.GeneratorService;
import qrng.PrimeService.Generator.GeneratorType;

@Service
public class PrimeService {
    
    private final GeneratorService generatorService;

    public PrimeService(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public List<BigInteger> generatePrime(List<String> trusted_generators, int n, int bits, int certainty) {
        
        if(trusted_generators.isEmpty()) {
            trusted_generators = generatorService.getAllGenerators().stream()
                .filter(generator -> generator.getType() == GeneratorType.QUANTUM)
                .map(generator -> generator.getName())
                .collect(Collectors.toList());
        }
        else {
            // confirm the generators exist before getting their values
            List<String> generators = generatorService.getAllGenerators().stream()
                .map(g -> g.getName())
                .collect(Collectors.toList());
            
            trusted_generators.stream().forEach(generator -> {
                if(!generators.contains(generator)) {
                    Generator newGenerator = generatorService.getGenerator(generator);
                    generatorService.addGenerator(newGenerator);
                    //throw new IllegalStateException(String.format("Generator '%s' does not exist", generator));
                }
            });
        }

        int each = (trusted_generators.size() > 1)? (n / trusted_generators.size() + ((n % trusted_generators.size() > 0)? 1: 0)): n;

        return trusted_generators.stream()
            .parallel()
            .map(
                generator ->
                    // generate the prime numbers from the seeds of the random number
                    generatorService.popRandomNumbers(generator, each)
                        .stream()
                        .map(number -> transform_to_prime(number, n, bits, certainty))
                        .collect(Collectors.toList())
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList())
            .subList(0, n);
    }

    public BigInteger transform_to_prime(BigInteger number, int n, int bits, int certainty) {
        return new BigInteger(bits, certainty, new Random(number.longValue()));
    }
    
}