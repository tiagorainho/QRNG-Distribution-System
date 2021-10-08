package qrng.PrimeService.Generator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import qrng.PrimeService.RN.RNService;

@Service
public class GeneratorServiceAsync {

    @Autowired
    private final GeneratorRepository generatorRepository;
    @Autowired
    private final RNService rnService;

    public GeneratorServiceAsync(GeneratorRepository generatorRepository, RNService rnService) {
        this.generatorRepository = generatorRepository;
        this.rnService = rnService;
    }

    @Async
    public void refillRandomNumbers(Generator generator, int n) {
        List<Long> rn = rnService.requestRandomNumbers(generator, n);
        generator.addRN(rn);
        generatorRepository.save(generator);
    }
}
