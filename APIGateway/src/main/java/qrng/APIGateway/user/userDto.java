package qrng.APIGateway.user;

import java.util.UUID;

public class userDto {
    private UUID id;
    private String name;
    private String email;
    private String API_KEY;

    public UUID getId() {
        return this.id;
    }
}
