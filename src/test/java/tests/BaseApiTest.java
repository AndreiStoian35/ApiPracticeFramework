package tests;

import api.BookingApiClient;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import context.TestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utils.ApiUtils;
import utils.BookingTestDataFactory;
import utils.ConfigReader;

public class BaseApiTest {
    protected Playwright playwright;
    protected APIRequestContext apiRequestContext;
    protected BookingApiClient bookingApi;
    protected TestContext testContext;
    protected ApiUtils apiUtils;


    @BeforeEach
    public void setUp(){
        playwright = Playwright.create();
        apiRequestContext = playwright.request().newContext();
        testContext = new TestContext();

        bookingApi = new BookingApiClient(apiRequestContext);

        apiUtils = new ApiUtils(bookingApi,testContext);


    }

    @AfterEach
    public void tearDown(){

        try{
            apiRequestContext.dispose();
        }finally{
            playwright.close();
            testContext.clear();
        }

    }

}
