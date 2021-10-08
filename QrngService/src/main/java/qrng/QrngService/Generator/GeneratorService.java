package qrng.QrngService.Generator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {

    private final long cache_size = 100;

    @Autowired
    private final GeneratorRepository generatorRepository;
    
    public GeneratorService(GeneratorRepository generatorRepository) {
        this.generatorRepository = generatorRepository;
    }

    public void registry(String name, String url, GeneratorType type) {
        generatorRepository.insert(new Generator(name, url, type, cache_size));
    }

    public List<Generator> generators() {
        return generatorRepository.findAll();
    }

    public List<String> generatorsName() {
        return generatorRepository.findAll().stream()
            .map(generator -> generator.getName())
            .toList();
    }


}
