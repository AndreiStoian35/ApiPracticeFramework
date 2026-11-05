package context;

import models.request.PostBookingRequestBody;
import models.respose.POST.PostBookingResponse;

public class TestContext {


    public Integer bookingId;

    public PostBookingRequestBody requestBookingBody = new PostBookingRequestBody();

    public PostBookingResponse postBookingResponseBody = new PostBookingResponse();

    public void clear(){
        bookingId = null;
        requestBookingBody = null;
    }

}
