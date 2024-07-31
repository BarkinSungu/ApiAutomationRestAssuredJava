package ApiTests;

import Common.CommonLib;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class BookingDeletion {

    CommonLib commonLib = new CommonLib();

    String baseURI = (String) commonLib.getDataFromDatasetJson("bookerURI");
    String authUserName = (String) commonLib.getDataFromDatasetJson("authUserName");
    String authPassword = (String) commonLib.getDataFromDatasetJson("authPassword");

    private String token;

    @BeforeClass
    public void setup() {
        // RestAssured base URI
        RestAssured.baseURI = baseURI;
    }

    @BeforeMethod
    public void getAuthToken() {
        // Request body parameters
        Map<String, String> authDetails = new HashMap<>();
        authDetails.put("username", authUserName);
        authDetails.put("password", authPassword);

        // POST request sending
        Response authResponse = given()
                .header("Content-Type", "application/json")
                .body(authDetails)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        // Printing the response to the console
        System.out.println("Auth Response: " + authResponse.asString());

        // // Retrieve the token
        token = authResponse.jsonPath().getString("token");
    }

    @Test(dependsOnMethods = "createBooking")
    public void deleteBooking() {
        // Get bookingId from BookingCreation
        int bookingId = BookingCreation.bookingId;

        // DELETE request sending and get response
        Response deleteResponse = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201)
                .extract()
                .response();

        // Printing the response to the console
        System.out.println("Delete Response: " + deleteResponse.asString());

        // Send a GET request to verify the accuracy of the deleted record.
        try {
            given()
                    .header("Content-Type", "application/json")
                    .when()
                    .get("/booking/" + bookingId)
                    .then()
                    .statusCode(404);

            System.out.println("Booking ID " + bookingId + " deleted successfully.");
        } catch (Exception e) {
            System.out.println("Received the expected 404 error: " + e.getMessage());
        }
    }
}
