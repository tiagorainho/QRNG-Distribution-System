package qrng.QrngService.Generator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qrng.QrngService.RandomNumbers.RandomNumbersService;

@Service
public class GeneratorService {

    private final long cache_size = 100;

    @Autowired
    private final GeneratorRepository generatorRepository;
    @Autowired
    private final RandomNumbersService randomNumbersService;
    
    public GeneratorService(GeneratorRepository generatorRepository, RandomNumbersService randomNumbersService) {
        this.generatorRepository = generatorRepository;
        this.randomNumbersService = randomNumbersService;
    }

    public void registry(String name, String url, GeneratorType type) {
        generatorRepository.insert(new Generator(name, url, type, cache_size));
        randomNumbersService.addRandomNumbers(name, List.of());
    }

    public List<Generator> generators() {
        return generatorRepository.findAll();
    }

    public List<String> generatorsName() {
        return generatorRepository.findAll().stream()
            .map(generator -> generator.getName())
            .collect(Collectors.toList());
    }

    public Generator getGeneratorByName(String name) {
        Optional<Generator> generator = generatorRepository.findGeneratorByName(name);
        if(generator.isPresent()) {
            return generator.get();
        }
        return null;
    }


}
