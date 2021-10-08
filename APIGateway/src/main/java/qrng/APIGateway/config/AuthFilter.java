package qrng.APIGateway.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.cloud.client.loadbalancer.ClientHttpResponseStatusCodeException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import qrng.APIGateway.user.userDto;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final WebClient.Builder webClientBuilder;
    
    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new IllegalStateException("Missing authorization information");
            }


            String apiKey = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            return webClientBuilder.build()
                .get()
                .uri("http://user-service/api/user/validateApiKey?API_Key=" + apiKey)
                .retrieve()
                /*
                .onStatus(HttpStatus.OK, response-> {
                    return Mono.error(new IllegalStateException("boas"));
                })
                */
                .bodyToMono(userDto.class)
                .map(userDto -> {
                    exchange.getRequest()
                        .mutate()
                        .header("X-auth-user-id", String.valueOf(userDto.getId()));
                    return exchange;
                })
                .flatMap(chain::filter);
        };

    }

    public static class Config {

    }

}