package qrng.QrngService.RandomNumbers;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import qrng.QrngService.Generator.Generator;
import qrng.QrngService.Generator.GeneratorRepository;

@Service
public class RandomNumbersService {

    @Autowired
    private final RandomNumbersDao randomNumbersDao;
    @Autowired
    private final GeneratorRepository generatorRepository;
    @Autowired
    private final RandomNumbersServiceAsync randomNumbersServiceAsync;

    public RandomNumbersService(@Qualifier("in_memory") RandomNumbersDao randomNumbersDao, GeneratorRepository generatorRepository, RandomNumbersServiceAsync randomNumbersServiceAsync) {
        this.randomNumbersDao = randomNumbersDao;
        this.generatorRepository = generatorRepository;
        this.randomNumbersServiceAsync = randomNumbersServiceAsync;
    }

    public List<BigInteger> getRandomNumbers(List<String> trusted_generators, int bitsToRetrieve, int n) {
        
        final List<Generator> allGenerators = this.generatorRepository.findAll();
        List<Generator> generators;

        if(trusted_generators.isEmpty()) {
            generators = allGenerators;
        }
        else {
            List<String> allGeneratorNames = allGenerators.stream()
                .map(g -> g.getName())
                .toList();
            // confirm the generators exist before getting their values
            trusted_generators.stream().forEach(generator -> {
                if(!allGeneratorNames.contains(generator))
                    throw new IllegalStateException(String.format("Generator '%s' does not exist", generator));
            });
            // remove extra generators
            generators = allGenerators.stream()
                .filter(generator -> trusted_generators.contains(generator.getName()))
                .toList();
        }

        // calculate how many random elements each generator has to get
        int each = (generators.size() > 1)? (n / generators.size() + ((n % generators.size() > 0)? 1: 0)): n;
        
        // calculate how many bytes each generator will use
        int bytesUsed = each*bitsToRetrieve/Byte.SIZE + ((each*bitsToRetrieve%Byte.SIZE)==0?0:1);

        // add bits to the queue in a non blocking way
        generators.stream()
            .parallel()
            .forEach((generator) -> randomNumbersServiceAsync.refillRandomNumbers(generator, bytesUsed));
            
        return generators.stream()
            .parallel()
            .map(generator -> {
                    int size = randomNumbersDao.size(generator.getName());
                    System.out.println("size of " + generator.getName() + ": " + size);
                    System.out.println("qnt precisa em bytes do " + generator.getName() + ": " + each*bitsToRetrieve/Byte.SIZE);
                    if(each*bitsToRetrieve/Byte.SIZE > size) {
                        List<Byte> a = randomNumbersServiceAsync.requestRandomBytes(generator.getUrl(), bytesUsed);
                        a.stream().forEach(System.out::println);
                        addRandomNumbers(generator.getName(), a);
                    }
                    return Stream.generate(
                        () -> this.randomNumbersDao.pop(generator.getName(), bitsToRetrieve)
                    )
                    .limit(each).toList();
                }
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList())
            .subList(0, n);
    }

    public void addRandomNumbers(String generator, List<Byte> bytes) {
        if(bytes == null) this.randomNumbersDao.add(generator);
        else this.randomNumbersDao.add(generator, bytes);
    }

    public void addRandomNumbers(String generator) {
        this.addRandomNumbers(generator, null);
    }
    
}


/*
        // get the proper trusted generators in case of default
        if(trusted_generators.isEmpty()) {
            trusted_generators = allGenerators;
        }
        else {
            // confirm the generators exist before getting their values            
            trusted_generators.stream().forEach(generator -> {
                if(!allGenerators.contains(generator))
                    throw new IllegalStateException(String.format("Generator '%s' does not exist", generator));
            });
        }

        int each = (trusted_generators.size() > 1)? (n / trusted_generators.size() + ((n % trusted_generators.size() > 0)? 1: 0)): n;

        /*
        trusted_generators.stream()
            .parallel()
            .forEach((generator) -> randomNumbersServiceAsync.refillRandomNumbers(generator, "localhost", each));
            
        return trusted_generators.stream()
            .parallel()
            .map(generator -> Stream.generate(
                    () -> {
                        return this.randomNumbersDao.pop(generator, bitsToRetrieve);
                    }
                )
                .limit(each).toList()
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList())
            .subList(0, n);
        */