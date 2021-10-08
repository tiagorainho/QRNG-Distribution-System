package qrng.PrimeService.Prime;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path="api/prime")
@RestController
public class PrimeController {
    
    private final PrimeService primeService;
    
    @Autowired
    public PrimeController(PrimeService primeService) {
        this.primeService = primeService;
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Map<String,List<BigInteger>>> getPrime(
        @RequestParam(required = false, name="trusted_generator")  List<String> trusted_generators,
        @RequestParam(required = false, defaultValue = "1") int n,
        @RequestParam(required = false, defaultValue = "32") int bits,
        @RequestParam(required = false, defaultValue = "20") int certainty
    ) {

        return new ResponseEntity<>(
            Map.of("primeNumbers", primeService.generatePrime(
                (trusted_generators == null)? List.of(): trusted_generators.stream().toList(),
                n,
                bits,
                certainty
            )),
            HttpStatus.OK
        );
    }

}
