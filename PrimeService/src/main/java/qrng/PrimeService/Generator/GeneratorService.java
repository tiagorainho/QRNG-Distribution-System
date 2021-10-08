package qrng.PrimeService.Generator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qrng.PrimeService.RN.RNService;

@Service
public class GeneratorService {
    
    @Autowired
    private final GeneratorRepository generatorRepository;
    @Autowired
    private final RNService rnService;
    @Autowired
    private final GeneratorServiceAsync generatorServiceAsync;

    public GeneratorService(GeneratorServiceAsync generatorServiceAsync, GeneratorRepository generatorRepository, RNService rnService) {
        this.generatorServiceAsync = generatorServiceAsync;
        this.generatorRepository = generatorRepository;
        this.rnService = rnService;
    }

    public List<Generator> getAllGenerators() {
        return this.generatorRepository.findAll();
    }

    public Generator getGenerator(String name) {
        Optional<Generator> generator = generatorRepository.findGeneratorByName(name);
        if(generator.isEmpty()) {
            return this.rnService.getGeneratorByName(name);
        }
        return generator
            .orElseThrow(
                () -> {
                    throw new IllegalStateException("Generator not found");
                }
            );
    }

    public void addGenerator(Generator generator) {
        generatorRepository.insert(generator);
    }

    public List<BigInteger> popRandomNumbers(String generator_name, int n) {
        Generator generator = getGenerator(generator_name);
        
        // garantee that exists enough number, otherwise get more from the random service
        long cacheSize = generator.getCacheSize();

        List<BigInteger> numbers = new ArrayList<>();

        if(n <= cacheSize && n <= generator.getRandomNumbers().size()) {
            // get random numbers stored in memory, remove them and save
            numbers = Stream.generate(
                () -> generator.getRandomNumbers().remove(0).getValue()
            )
            .limit(n)
            .collect(Collectors.toList());
            
            generatorRepository.save(generator);

            // non blocking call
            generatorServiceAsync.refillRandomNumbers(generator, n);
            //generator.addRN(rnService.getRandomNumbers(generator_name, n));
        }
        else {
            // get all the random numbers stored in memory, remove them and save
            numbers = Stream.generate(
                    () -> generator.getRandomNumbers().remove(0).getValue()
                )
                .limit(generator.getRandomNumbers().size())
                .collect(Collectors.toList());

            generatorRepository.save(generator);
            
            // blocking call
            numbers.addAll(
                rnService.requestRandomNumbers(generator, n - numbers.size()).stream()
                    .map(rn -> new BigInteger(String.valueOf(rn)))
                    .toList()
            );

            // non blocking call
            generatorServiceAsync.refillRandomNumbers(generator, (int)generator.getCacheSize());
            //generator.addRN(rnService.getRandomNumbers(generator_name, n));
        }
        return numbers;
    }

    



}
