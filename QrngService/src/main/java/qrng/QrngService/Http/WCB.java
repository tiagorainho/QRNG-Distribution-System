package qrng.QrngService.Http;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WCB {
    
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
}
