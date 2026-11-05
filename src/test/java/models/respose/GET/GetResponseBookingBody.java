package models.respose.GET;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetResponseBookingBody {

    @JsonProperty("firstname")
    public String firstName;

    @JsonProperty("lastname")
    public String lastName;

    @JsonProperty("totalprice")
    public Integer totalPrice;

    @JsonProperty("depositpaid")
    public Boolean depositPaid;

    @JsonProperty("bookingdates")
    public BookingDates bookingDates = new BookingDates();


    public static class BookingDates{

        @JsonProperty("checkin")
        public String checkIn;

        @JsonProperty("checkout")
         public String checkOut;
    }

    @JsonProperty("additionalneeds")
    public String additionalNeeds;

}
