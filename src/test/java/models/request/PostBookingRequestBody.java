package models.request;

public class PostBookingRequestBody {

    public String firstname;

    public String lastname;

    public Integer totalprice;

    public Boolean depositpaid;

    public BookingDates bookingdates = new BookingDates();


    public static class BookingDates{

        public String checkin;

        public String checkout;
    }

    public String additionalneeds;
}
