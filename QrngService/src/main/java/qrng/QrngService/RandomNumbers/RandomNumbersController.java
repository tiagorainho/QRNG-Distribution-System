package qrng.QrngService.RandomNumbers;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/random/")
public class RandomNumbersController {

    @Autowired
    private final RandomNumbersService randomNumbersService;

    public RandomNumbersController(RandomNumbersService randomNumbersService) {
        this.randomNumbersService = randomNumbersService;
    }
    
    @GetMapping
    public ResponseEntity<Map<String, List<BigInteger>>> getRandomNumbers(
        @RequestParam(required = false, name="trusted_generator")  List<String> trusted_generators,
        @RequestParam(required = false, defaultValue = "32") int bits,
        @RequestParam(required = false, defaultValue = "1") int n
    ) {

        return new ResponseEntity<>(
            Map.of("numbers", this.randomNumbersService.getRandomNumbers(
                (trusted_generators == null)? List.of(): trusted_generators.stream().toList(),
                bits,
                n)
            ),
            HttpStatus.OK
        );
    }

}