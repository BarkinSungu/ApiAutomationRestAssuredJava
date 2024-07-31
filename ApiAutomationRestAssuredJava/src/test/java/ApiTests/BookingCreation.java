package ApiTests;

import Common.CommonLib;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BookingCreation {

    CommonLib commonLib = new CommonLib();

    public static int bookingId;

    String baseURI = (String) commonLib.getDataFromDatasetJson("bookerURI");
    String firstName = (String) commonLib.getDataFromDatasetJson("firstName");
    String lastName = (String) commonLib.getDataFromDatasetJson("lastName");
    int totalPrice = (int) commonLib.getDataFromDatasetJson("totalPrice");
    boolean depositePaid = (boolean) commonLib.getDataFromDatasetJson("depositePaid");
    String checkinDate = (String) commonLib.getDataFromDatasetJson("checkinDate");
    String checkoutDate = (String) commonLib.getDataFromDatasetJson("checkoutDate");
    String additionalNeeds = (String) commonLib.getDataFromDatasetJson("additionalNeeds");

    @BeforeClass
    public void setup() {
        // RestAssured base URI
        RestAssured.baseURI = baseURI;
    }

    @Test()
    public void createBooking() {
        // Request body parameters
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("firstname", firstName);
        bookingDetails.put("lastname", lastName);
        bookingDetails.put("totalprice", totalPrice);
        bookingDetails.put("depositpaid", depositePaid);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", checkinDate);
        bookingDates.put("checkout", checkoutDate);

        bookingDetails.put("bookingdates", bookingDates);
        bookingDetails.put("additionalneeds", additionalNeeds);

        // POST request sending
        Response createResponse = given()
                .header("Content-Type", "application/json")
                .body(bookingDetails)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo(firstName))
                .body("booking.lastname", equalTo(lastName))
                .body("booking.totalprice", equalTo(totalPrice))
                .body("booking.depositpaid", equalTo(depositePaid))
                .body("booking.bookingdates.checkin", equalTo(checkinDate))
                .body("booking.bookingdates.checkout", equalTo(checkoutDate))
                .body("booking.additionalneeds", equalTo(additionalNeeds))
                .body("bookingid", notNullValue())
                .extract()
                .response();

        // Printing the response to the console
        System.out.println("Create Response: " + createResponse.asString());

        // Retrieve the newly created booking ID
        bookingId = createResponse.jsonPath().getInt("bookingid");
    }
}
