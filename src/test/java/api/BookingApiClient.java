package api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Step;
import utils.ConfigReader;


public class BookingApiClient {

    private final APIRequestContext apiRequestContext;

    public BookingApiClient(APIRequestContext context){
        this.apiRequestContext = context;
    }

    @Step("GET/booking")
    public APIResponse getAllBookings(){
        return apiRequestContext.get(ConfigReader.getBaseUrl() +"/booking");
    }

    @Step("GET/booking/id")
    public APIResponse getBookingId(int id){

        return apiRequestContext.get(ConfigReader.getBaseUrl()  +"/booking/" + id);}

    @Step("POST/booking")
    public APIResponse postBooking(RequestOptions requestOptions){
        return apiRequestContext.post(ConfigReader.getBaseUrl()  +"/booking",requestOptions);}

    @Step("Create Token request")
    public APIResponse createToken(RequestOptions requestOptions){return apiRequestContext.post(ConfigReader.getBaseUrl() + "/auth", requestOptions);}

    @Step("PUT/booking/id")
    public APIResponse updateBooking(int bookingId, RequestOptions requestOptions){return apiRequestContext.put(ConfigReader.getBaseUrl() + "/booking/" + bookingId, requestOptions);}

    @Step("DELETE/booking/id")
    public APIResponse deleteBooking(int bookingId, RequestOptions requestOptions){return apiRequestContext.delete(ConfigReader.getBaseUrl()  + "/booking/" + bookingId, requestOptions);}

    @Step("PATCH/booking/id")
    public APIResponse partialUpdateBooking(int bookingId,RequestOptions requestOptions){return apiRequestContext.patch(ConfigReader.getBaseUrl()  + "/booking/" + bookingId, requestOptions);}

    public APIResponse getTestData(){
        return apiRequestContext.get(ConfigReader.getTestDataUrl());
    }

    public APIResponse getBookingsByName(String firstName, String lastName){
        return  apiRequestContext.get(ConfigReader.getBaseUrl() + "/booking",RequestOptions.create()
                .setQueryParam("firstname", firstName)
                .setQueryParam("lastname", lastName));

    }

}
