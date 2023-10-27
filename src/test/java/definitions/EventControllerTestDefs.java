package definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerTestDefs extends TestSetupDefs {

  private Response response;
  private JSONObject requestBody;

  @Given("The create event endpoint is {string}")
  public void theCreateEventEndpointIs(String endpoint) {
    RestAssured.baseURI = BASE_URL + port + createEventEndpoint;
  }


  @When("the client makes a POST request with event details")
  public void theClientMakesAPOSTRequestWithEventDetails() throws JSONException {
    requestBody = new JSONObject();
    requestBody.put("name", "Event Name");
    requestBody.put("eventDate", "2023-11-01");
    requestBody.put("venue", "Event Venue");
    requestBody.put("description", "Event Description");

    response = RestAssured.given()
      .contentType(ContentType.JSON)
      .body(requestBody.toString())
      .post(BASE_URL + port + createEventEndpoint);
  }


  @Then("the response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
    int actualStatusCode = response.getStatusCode();

    assertEquals(expectedStatusCode, actualStatusCode);
  }


  @Then("the response should contain the created event details")
  public void theResponseShouldContainTheCreatedEventDetails() {
    Assert.assertEquals("success", response.jsonPath().getString("message"));

    Assert.assertEquals("Event Name", response.jsonPath().getString("data.name"));
    Assert.assertEquals("2023-11-01", response.jsonPath().getString("data.eventDate"));
    Assert.assertEquals("Event Venue", response.jsonPath().getString("data.venue"));
    Assert.assertEquals("Event Description", response.jsonPath().getString("data.description"));
  }


}
