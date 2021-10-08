package qrng.PrimeService.Generator;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneratorRepository extends MongoRepository<Generator, String> {
    
    Optional<Generator> findGeneratorByName(String name);

    List<Generator> findAll();

}
