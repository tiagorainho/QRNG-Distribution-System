package qrng.PrimeService.RN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qrng.PrimeService.Generator.Generator;


@Service
public class RNService {
    
    @Autowired
    private RNClientInterface rnClient;

    public RNService(RNClient rnClient) {
        this.rnClient = rnClient;
    }

    public List<Long> requestRandomNumbers(Generator generator, long n) {
        return this.rnClient.requestRandomNumbers(generator, n);
        //return Stream.generate(() -> new BigInteger(64, new Random()).longValue()).limit(n).collect(Collectors.toList());
    }

    public Generator getGeneratorByName(String name) {
        return this.rnClient.getGeneratorByName(name);
    }

}
