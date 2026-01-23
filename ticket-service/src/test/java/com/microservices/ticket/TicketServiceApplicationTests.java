package com.microservices.ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.hamcrest.Matchers;
import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer postgresContiner = new PostgreSQLContainer("postgres:15")
			.withDatabaseName("ticket_service")
            .withUsername("root")
            .withPassword("bondstone");
	@LocalServerPort
	private Integer port;
	
	static {
		postgresContiner.start();
	}
	
	@BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
	
	
	@Test
    void shouldCreateTicket() {
        String requestBody = """
            {
              "subject": "new second ticket",
              "description": "this is a test ticket",
              "priority": "HIGH"
            }
        """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/tickets")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("subject", Matchers.equalTo("new second ticket"))
                .body("description", Matchers.equalTo("this is a test ticket"))
                .body("priority", Matchers.equalTo("HIGH"));
    }
	
	@Test
	void shouldUpdateTicket() {
	    String createBody = """
	        {
	          "subject": "original",
	          "description": "original desc",
	          "priority": "HIGH"
	        }
	    """;

	    var createResponse =
	        RestAssured.given()
	            .contentType("application/json")
	            .body(createBody)
	            .post("/api/tickets")
	            .then()
	            .statusCode(201)
	            .extract();

	    Long id = ((Number) createResponse.path("id")).longValue();

	    String updateBody = """
	        {
	          "subject": "updated",
	          "description": "updated desc",
	          "priority": "LOW"
	        }
	    """;

	    RestAssured.given()
	        .contentType("application/json")
	        .body(updateBody)
	        .when()
	        .put("/api/tickets/" + id)
	        .then()
	        .statusCode(200)
	        .body("subject", Matchers.equalTo("updated"))
	        .body("priority", Matchers.equalTo("LOW"));
	}

	
	
	@Test
	void shouldDeleteTicket() {
	    String body = """
	        {
	          "subject": "to delete",
	          "description": "bye",
	          "priority": "MEDIUM"
	        }
	    """;

	    var response =
	        RestAssured.given()
	            .contentType("application/json")
	            .body(body)
	            .post("/api/tickets")
	            .then()
	            .statusCode(201)
	            .extract();

	    Long id = ((Number) response.path("id")).longValue();

	    RestAssured.delete("/api/tickets/" + id)
	        .then()
	        .statusCode(204);

	    RestAssured.get("/api/tickets/" + id)
	        .then()
	        .statusCode(404);
	}



	
	

}
