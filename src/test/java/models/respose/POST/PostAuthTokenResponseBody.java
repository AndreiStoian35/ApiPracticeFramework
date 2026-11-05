package models.respose.POST;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostAuthTokenResponseBody {

    @JsonProperty("token")
    public String token;
}
