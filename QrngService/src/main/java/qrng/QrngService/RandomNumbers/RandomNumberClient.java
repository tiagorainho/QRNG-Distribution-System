package qrng.QrngService.RandomNumbers;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class RandomNumberClient implements RandomNumberClientInterface {
    
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    @Autowired
    private WebClient.Builder webClientBuilder;

    public RandomNumberClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public List<Byte> requestRandomBytes(String url, int n) {
        return webClientBuilder
            .baseUrl(url)
            .build()
            .get()
            .uri(uri -> uri
                .path("/random")
                .queryParam("bytes", 1)
                .queryParam("n", n)
                .build()
            )
            .retrieve()
            .bodyToMono(RandomNumberDto.class)
            .block(REQUEST_TIMEOUT)
            .getNumbers();
    }

}
