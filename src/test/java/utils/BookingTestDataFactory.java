package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.playwright.APIResponse;
import context.TestContext;
import models.request.PostBookingRequestBody;
import models.respose.GET.GetUserTestData;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BookingTestDataFactory {

    public PostBookingRequestBody createDefaultBookingData(GetUserTestData dynamicData ) throws JsonProcessingException {

        Random random = new Random();

        int checkInYear = 2022 + random.nextInt(3); // 2022–2024
        Integer month = 1 + random.nextInt(12);
        Integer day = 1 + random.nextInt(28);
        String checkInDate = checkInYear + "-"
                + addZeroDate(month) + "-"
                + addZeroDate(day);


        String checkOutDate = (2026 + random.nextInt(2)) + "-"
                + addZeroDate(month) + "-"
                + addZeroDate(day);


        List<String> listOfNeeds = List.of("Lunch", "Dinner", "Breakfast");

        PostBookingRequestBody bookingRequestBody = new PostBookingRequestBody();
        bookingRequestBody.firstname = dynamicData.results.get(0).name.first;
        bookingRequestBody.lastname = dynamicData.results.get(0).name.last;
        bookingRequestBody.totalprice = 10 + random.nextInt(91);
        bookingRequestBody.depositpaid = random.nextBoolean();
        bookingRequestBody.bookingdates.checkin = checkInDate;
        bookingRequestBody.bookingdates.checkout = checkOutDate;
        bookingRequestBody.additionalneeds = listOfNeeds.get(random.nextInt(listOfNeeds.size()));

        return bookingRequestBody;
    }

    private String addZeroDate(Integer number){
        if( number < 10){
            return "0" + number;
        }
        return String.valueOf(number);
    }
}
