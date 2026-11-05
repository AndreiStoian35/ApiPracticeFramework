package models.respose.POST;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.respose.GET.GetResponseBookingBody;

public class PostBookingResponse {

    @JsonProperty("bookingid")
    public Integer bookingId;


    @JsonProperty("booking")
    public GetResponseBookingBody booking = new GetResponseBookingBody();
}
