package definitions;

import com.example.cebackend.CeBackendApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CeBackendApplication.class)
public class TestSetupDefs {
  // Base URL for testing
  public static final String BASE_URL = "http://localhost:";

  // Authentication Endpoints
  public static final String helloEndpoint = "/auth/users/hello/";
  public static final String registerEndpoint = "/auth/users/register/";
  public static final String loginEndpoint = "/auth/users/login/";
}
