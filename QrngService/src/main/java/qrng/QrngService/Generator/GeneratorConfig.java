package qrng.QrngService.Generator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import qrng.QrngService.RandomNumbers.RandomNumbersService;

@Configuration
public class GeneratorConfig {

    @Bean
    CommandLineRunner runner(GeneratorRepository generatorRepository, MongoTemplate mongoTemplate, RandomNumbersService randomNumbersService) {
        return args -> {
            generatorRepository.deleteAll();
            addGenerator(generatorRepository, randomNumbersService, new Generator("IT QRNG", "localhost", GeneratorType.QUANTUM, 20));
            addGenerator(generatorRepository, randomNumbersService, new Generator("IT QRNG 2", "local", GeneratorType.PSEUDO_RANDOM, 20));
        };
    }
    
    private void addGenerator(GeneratorRepository generatorRepository, RandomNumbersService randomNumbersService, Generator generator) {
        generatorRepository.insert(generator);
        //byte[] initial_byte_array = new byte[] { 72, 101, 14, 108, 111, 32, 61, 21, 43, 69, 04, 90 };

        int toInsert = (int)generator.getCacheSize();
        byte[] initial_byte_array = new byte[toInsert];
        new Random().nextBytes(initial_byte_array);

        List<Byte> list =  new ArrayList<>();
        for(byte b: initial_byte_array) {
            list.add(Byte.valueOf(b));
        }
        randomNumbersService.addRandomNumbers(generator.getName(), list);
        //List<BigInteger> listOfBigIntegers = randomNumbersService.getRandomNumbers(List.of(generator.getName()), 7, 2);

        //listOfBigIntegers.stream().forEach(x -> System.out.print(x + ", "));
        System.out.println(String.format("Generator %s ready.", generator.getName()));
    }

}
