package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Allure;
import models.request.PostBookingRequestBody;
import models.respose.GET.GetResponseBookingBody;
import models.respose.GET.GetResponseBookingId;
import models.respose.POST.PostBookingResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class BookingApiTests extends BaseApiTest{

    ObjectMapper mapper = new ObjectMapper();
    @Test
    void shouldRetrieveAllBookings() throws JsonProcessingException {
        APIResponse response = bookingApi.getAllBookings();
        Assertions.assertEquals(200, response.status());

        String responseBody = response.text();


        Assertions.assertFalse(responseBody.isBlank());


        List<GetResponseBookingId> list = mapper.readValue(responseBody, new TypeReference<List<GetResponseBookingId>>(){});
        List<Integer> idList = new ArrayList<>();

        Assertions.assertFalse(list.isEmpty());

        Set<Integer> set = new LinkedHashSet<>();

        for (GetResponseBookingId id: list){
            Assertions.assertTrue(id.bookingId > 0);
            idList.add(id.bookingId);
            set.add(id.bookingId);
        }

        List<Integer> newList = new ArrayList<>(set);

        Assertions.assertEquals(newList,idList);

    }


    @Test
    public void postAndVerifyBookingResponse() throws JsonProcessingException {

        APIResponse response = apiUtils.createBooking();


        Assertions.assertEquals(200, response.status());

        String responseBody = response.text();

        PostBookingResponse responseBookingBody = mapper.readValue(responseBody, PostBookingResponse.class);

        PostBookingRequestBody postBodyToVerify = testContext.requestBookingBody;

        Allure.step("Compare booking body request vs response");
        Assertions.assertTrue(responseBookingBody.bookingId > 0);
        Assertions.assertEquals(postBodyToVerify.firstname, responseBookingBody.booking.firstName);
        Assertions.assertEquals(postBodyToVerify.lastname, responseBookingBody.booking.lastName);
        Assertions.assertEquals(postBodyToVerify.totalprice, responseBookingBody.booking.totalPrice);
        Assertions.assertEquals(postBodyToVerify.depositpaid,responseBookingBody.booking.depositPaid);
        Assertions.assertEquals(postBodyToVerify.bookingdates.checkin, responseBookingBody.booking.bookingDates.checkIn);
        Assertions.assertEquals(postBodyToVerify.bookingdates.checkout, responseBookingBody.booking.bookingDates.checkOut);
        Assertions.assertEquals(postBodyToVerify.additionalneeds,responseBookingBody.booking.additionalNeeds);

        String authToken = apiUtils.getAuthToken();

        Allure.step("DELETING booking..");
        APIResponse deleteBookingResponse = bookingApi.deleteBooking(responseBookingBody.bookingId,
                RequestOptions.create()
                        .setHeader("Cookie", "token=" + authToken));

        Assertions.assertEquals(201,deleteBookingResponse.status());

    }




    @Test
    public void getAndVerifyBookingResponse() throws JsonProcessingException {

        APIResponse createBookingResponse = apiUtils.createBooking();
        PostBookingResponse responseBookingBody = mapper.readValue(createBookingResponse.text(), PostBookingResponse.class);


        APIResponse getBookingResponse = bookingApi.getBookingId(responseBookingBody.bookingId);
        Assertions.assertEquals(200,getBookingResponse.status());
        String responseBody = getBookingResponse.text();

        GetResponseBookingBody body = mapper.readValue(responseBody, GetResponseBookingBody.class);

        PostBookingRequestBody postBodyToVerify = testContext.requestBookingBody;

        Allure.step("Comparing booking POST body vs GET response body");
        Assertions.assertEquals(postBodyToVerify.firstname, body.firstName);
        Assertions.assertEquals(postBodyToVerify.lastname, body.lastName);
        Assertions.assertEquals(postBodyToVerify.totalprice, body.totalPrice);
        Assertions.assertEquals(postBodyToVerify.depositpaid,body.depositPaid);
        Assertions.assertEquals(postBodyToVerify.bookingdates.checkin, body.bookingDates.checkIn);
        Assertions.assertEquals(postBodyToVerify.bookingdates.checkout, body.bookingDates.checkOut);

    }

    @Test
    public void updateBookingWithAuthAndCompareBody() throws JsonProcessingException {
        APIResponse createBookingResponse = apiUtils.createBooking();
        Assertions.assertEquals(200, createBookingResponse.status());


        String authToken = apiUtils.getAuthToken();


        PostBookingResponse bookingResponseBody = mapper.readValue(createBookingResponse.text(), PostBookingResponse.class);
        PostBookingRequestBody updateBooking = new PostBookingRequestBody();

        Allure.step("Setting new data");
        updateBooking.firstname = bookingResponseBody.booking.firstName;
        updateBooking.lastname = bookingResponseBody.booking.lastName;
        updateBooking.depositpaid = bookingResponseBody.booking.depositPaid;
        updateBooking.additionalneeds = bookingResponseBody.booking.additionalNeeds;
        updateBooking.bookingdates.checkin = bookingResponseBody.booking.bookingDates.checkIn;
        updateBooking.bookingdates.checkout = bookingResponseBody.booking.bookingDates.checkOut;
        updateBooking.totalprice = 77;

        String json = mapper.writeValueAsString(updateBooking);


        APIResponse updateBookingResponse = bookingApi.updateBooking(bookingResponseBody.bookingId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept","application/json")
                        .setHeader("Cookie","token=" + authToken)
                        .setData(json));

        Assertions.assertEquals(200,updateBookingResponse.status());

        GetResponseBookingBody updateResponseBody = mapper.readValue(updateBookingResponse.text(), GetResponseBookingBody.class);

        Allure.step("Comparing fully updated body request with GET booking body response");
        Assertions.assertEquals(updateResponseBody.firstName, bookingResponseBody.booking.firstName);
        Assertions.assertEquals(updateResponseBody.lastName, bookingResponseBody.booking.lastName);
        Assertions.assertEquals(updateResponseBody.depositPaid, bookingResponseBody.booking.depositPaid);
        Assertions.assertEquals(updateResponseBody.additionalNeeds, bookingResponseBody.booking.additionalNeeds);
        Assertions.assertNotEquals(updateResponseBody.totalPrice,bookingResponseBody.booking.totalPrice);


        Allure.step("DELETING booking..");
        APIResponse deleteBookingResponse = bookingApi.deleteBooking(bookingResponseBody.bookingId,
                RequestOptions.create()
                        .setHeader("Cookie", "token=" + authToken));

        Assertions.assertEquals(201,deleteBookingResponse.status());

    }


    @Test
    public void updateBookingNoAuth() throws JsonProcessingException {
        APIResponse createBookingResponse = apiUtils.createBooking();
        Assertions.assertEquals(200, createBookingResponse.status());


        PostBookingResponse bookingResponseBody = mapper.readValue(createBookingResponse.text(), PostBookingResponse.class);
        PostBookingRequestBody updateBooking = new PostBookingRequestBody();

        Allure.step("Setting new data", ()->{
            updateBooking.firstname = bookingResponseBody.booking.firstName;
            updateBooking.lastname = bookingResponseBody.booking.lastName;
            updateBooking.depositpaid = bookingResponseBody.booking.depositPaid;
            updateBooking.additionalneeds = bookingResponseBody.booking.additionalNeeds;
            updateBooking.bookingdates.checkin = bookingResponseBody.booking.bookingDates.checkIn;
            updateBooking.bookingdates.checkout = bookingResponseBody.booking.bookingDates.checkOut;
            updateBooking.totalprice = 77;
        });


        String json = mapper.writeValueAsString(updateBooking);


        APIResponse updateBookingResponse = bookingApi.updateBooking(bookingResponseBody.bookingId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept","application/json")
                        .setData(json));

        Allure.step("Asserting booking can't be updated without auth token");
        Assertions.assertEquals(403,updateBookingResponse.status());


        Allure.step("Adding auth and DELETING booking..");
        String authToken = apiUtils.getAuthToken();

        APIResponse deleteBookingResponse = bookingApi.deleteBooking(bookingResponseBody.bookingId,
                RequestOptions.create()
                        .setHeader("Cookie", "token=" + authToken));

        Assertions.assertEquals(201,deleteBookingResponse.status());


    }


    @Test
    public void deleteBooking() throws JsonProcessingException {
        APIResponse createBookingResponse = apiUtils.createBooking();
        Assertions.assertEquals(200, createBookingResponse.status());
        PostBookingResponse postBookingResponse = mapper.readValue(createBookingResponse.text(), PostBookingResponse.class);

        String authToken = apiUtils.getAuthToken();

        Allure.step("DELETING booking..");
        APIResponse deleteBookingResponse = bookingApi.deleteBooking(postBookingResponse.bookingId,
                RequestOptions.create()
                .setHeader("Cookie", "token=" + authToken));

        Assertions.assertEquals(201,deleteBookingResponse.status());

        APIResponse getActualBooking = bookingApi.getBookingId(postBookingResponse.bookingId);

        Allure.step("Verifying that booking has been deleted");
        Assertions.assertEquals(404,getActualBooking.status());

    }

    @Test
    public void partiallyUpdateBooking() throws JsonProcessingException {
        APIResponse createBookingResponse = apiUtils.createBooking();
        Assertions.assertEquals(200, createBookingResponse.status());
        PostBookingResponse postBookingResponse = mapper.readValue(createBookingResponse.text(), PostBookingResponse.class);

        String authToken = apiUtils.getAuthToken();


        Map<String,Object> data = new HashMap<>();
        data.put("firstname", "PatchedFirstName");
        data.put("lastname", "PatchedLastname");

        String json = mapper.writeValueAsString(data);


        APIResponse patchBooking = bookingApi.partialUpdateBooking(postBookingResponse.bookingId,
                RequestOptions.create()
                        .setHeader("Cookie", "token=" + authToken)
                        .setHeader("Content-Type", "application/json")
                        .setData(json));

        System.out.println(patchBooking.text());

        Assertions.assertEquals(200, patchBooking.status());
        GetResponseBookingBody bodyAfterPatch = mapper.readValue(patchBooking.text(), GetResponseBookingBody.class);

        Allure.step("Validating that booking was partially updated");
        Assertions.assertEquals("PatchedFirstName", bodyAfterPatch.firstName);
        Assertions.assertEquals("PatchedLastname", bodyAfterPatch.lastName);
        Assertions.assertEquals(postBookingResponse.booking.totalPrice, bodyAfterPatch.totalPrice);
        Assertions.assertEquals(postBookingResponse.booking.bookingDates.checkIn, bodyAfterPatch.bookingDates.checkIn);
        Assertions.assertEquals(postBookingResponse.booking.bookingDates.checkOut, bodyAfterPatch.bookingDates.checkOut);
        Assertions.assertEquals(postBookingResponse.booking.additionalNeeds, bodyAfterPatch.additionalNeeds);


        Allure.step("Adding auth and DELETING booking..");
        APIResponse deleteBookingResponse = bookingApi.deleteBooking(postBookingResponse.bookingId,
                RequestOptions.create()
                        .setHeader("Cookie", "token=" + authToken));

        Assertions.assertEquals(201,deleteBookingResponse.status());

    }

    @Test
    public void shouldGetBookingByUsername() throws JsonProcessingException {

        APIResponse createBookingResponse = apiUtils.createBooking();
        Assertions.assertEquals(200, createBookingResponse.status());
        PostBookingResponse postBookingResponse = mapper.readValue(createBookingResponse.text(), PostBookingResponse.class);

        APIResponse getBooking = bookingApi.getBookingsByName(postBookingResponse.booking.firstName,
                postBookingResponse.booking.lastName);

        List<GetResponseBookingId> getBookingBodyIdList = mapper.readValue(getBooking.text(),new TypeReference<List<GetResponseBookingId>>(){});

        Assertions.assertFalse(getBookingBodyIdList.isEmpty());
        Assertions.assertTrue(getBookingBodyIdList.stream().anyMatch(item -> postBookingResponse.bookingId.equals(item.bookingId)));

        String authToken = apiUtils.getAuthToken();
        Allure.step("Adding auth and DELETING booking..");
        APIResponse deleteBookingResponse = bookingApi.deleteBooking(postBookingResponse.bookingId,
                RequestOptions.create()
                        .setHeader("Cookie", "token=" + authToken));

        Assertions.assertEquals(201,deleteBookingResponse.status());
    }


    @Test
    public void shouldReturn404(){

        APIResponse response = bookingApi.getBookingId(99999);

        Assertions.assertEquals(404,response.status());
    }




}
