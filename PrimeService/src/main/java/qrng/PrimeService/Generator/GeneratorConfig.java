package qrng.PrimeService.Generator;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import qrng.PrimeService.RN.RN;

@Configuration
public class GeneratorConfig {

    @Bean
    CommandLineRunner runner(GeneratorRepository generatorRepository, MongoTemplate mongoTemplate) {
        return args -> {
            generatorRepository.deleteAll();
            addGenerator(generatorRepository, new Generator("IT QRNG", "localhost:8082/api/random/", GeneratorType.QUANTUM, 100));
            addGenerator(generatorRepository, new Generator("IT QRNG 2", "localhost:8082/api/random/", GeneratorType.PSEUDO_RANDOM, 100));
        };
    }

    private void addGenerator(GeneratorRepository generatorRepository, Generator generator) {
        List<RN> rns = Stream.generate(() -> new RN(new BigInteger(64, new Random()))).limit(generator.getCacheSize()).toList();
        rns.forEach(rn -> generator.addRN(rn));
        generatorRepository.findGeneratorByName(generator.getName());
        generatorRepository.insert(generator);
        System.out.println(String.format("Generator %s ready.", generator.getName()));
    }
    
}
