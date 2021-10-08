package qrng.PrimeService.RN;

import java.time.Duration;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import qrng.PrimeService.Generator.Generator;

@Service
public class RNClient implements RNClientInterface {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    @Autowired
    private WebClient.Builder webClientBuilder;


    public RNClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    
    public List<Long> requestRandomNumbers(Generator generator, long n) {
        return webClientBuilder
            .baseUrl("http://qrng-service/")
            .build()
            .get()
            .uri(uri -> uri
                .path("api/random/")
                .queryParam("trusted_generator", generator.getName())
                .queryParam("bits", 32)
                .queryParam("n", n)
                .build()
            )
            .retrieve()
            .bodyToMono(RNDto.class)
            .timeout(REQUEST_TIMEOUT)
            .block()
            .getNumbers();
    }
    

}
