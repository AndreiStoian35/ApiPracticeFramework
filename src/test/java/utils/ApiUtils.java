package utils;

import api.BookingApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import context.TestContext;
import io.qameta.allure.Step;
import models.request.PostAuthTokenBody;
import models.request.PostBookingRequestBody;
import models.respose.GET.GetUserTestData;
import models.respose.POST.PostAuthTokenResponseBody;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import tests.BaseApiTest;

public class ApiUtils {

    private final BookingApiClient bookingApiClient;
    private final TestContext testContext;
    private final ObjectMapper mapper = new ObjectMapper();


    public ApiUtils(BookingApiClient bookingApiClient, TestContext testContext){
        this.bookingApiClient = bookingApiClient;
        this.testContext = testContext;
    }


    @Step("Return Auth Token")
    public String getAuthToken() throws JsonProcessingException {

        PostAuthTokenBody authTokenBody = new PostAuthTokenBody();
        authTokenBody.username = ConfigReader.getAuthUsername();
        authTokenBody.password = ConfigReader.getAuthPassword();

        String authReqBodyJson = mapper.writeValueAsString(authTokenBody);

        APIResponse authResponse = bookingApiClient.createToken(RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setData(authReqBodyJson));

        Assertions.assertEquals(200,authResponse.status());

        PostAuthTokenResponseBody auth = mapper.readValue(authResponse.text(), PostAuthTokenResponseBody.class);

        Assertions.assertNotNull(auth.token);
        Assertions.assertFalse(auth.token.isBlank());

        return auth.token;
    }


    public GetUserTestData getTestData () throws JsonProcessingException {

        APIResponse response = bookingApiClient.getTestData();

        Assertions.assertEquals(200,response.status());

        GetUserTestData data = mapper.readValue(response.text(), GetUserTestData.class);


        return  data;

    }
    public APIResponse createBooking() throws JsonProcessingException {

        BookingTestDataFactory testDataFactory = new BookingTestDataFactory();


        PostBookingRequestBody requestBookingBody = testDataFactory.createDefaultBookingData(getTestData());


        testContext.requestBookingBody = requestBookingBody;

        String json = mapper.writeValueAsString(requestBookingBody);



        return bookingApiClient.postBooking(RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setData(json));
    }

}
