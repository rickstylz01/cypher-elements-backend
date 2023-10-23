package definitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserControllerTestDefs extends TestSetupDefs {
  private static Response response;
  @Given("A valid public endpoint")
  public void aValidPublicEndpoint() {
    ResponseEntity<String> responseEntity = new RestTemplate().exchange(BASE_URL + port + helloEndpoint, HttpMethod.GET, null, String.class);
    Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @When("I say hello")
  public void iSayHello() {
    RequestSpecification request = RestAssured.given();
    request.header("Content-Type", "application/json");
    response = request.get(BASE_URL + port + helloEndpoint);
  }

  @Then("Hello is shown")
  public void helloIsShown() {
    JsonPath jsonPath = response.jsonPath();
    String message = jsonPath.get("message");
    Assert.assertEquals(200, response.getStatusCode());
    Assert.assertEquals("Hello", message);
  }
}
