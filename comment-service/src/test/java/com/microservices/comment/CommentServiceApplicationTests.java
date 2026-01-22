package com.microservices.comment;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CommentServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer postgresContiner = new PostgreSQLContainer("postgres:15")
			.withDatabaseName("comment_service")
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
    void shouldCreateAndGetComment() {
        String body = """
        {
          "ticketId": 1,
          "author": "Sam",
          "message": "First comment"
        }
        """;

        RestAssured.given()
                .contentType("application/json")
                .body(body)
                .post("/api/comments")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("author", Matchers.equalTo("Sam"));

        RestAssured.get("/api/comments?ticketId=1")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(1));
    }

    @Test
    void shouldDeleteComment() {
        String body = """
        {
          "ticketId": 2,
          "author": "Sam",
          "message": "To delete"
        }
        """;

        var res = RestAssured.given()
                .contentType("application/json")
                .body(body)
                .post("/api/comments")
                .then()
                .statusCode(201)
                .extract();

        Long id = ((Number) res.path("id")).longValue();

        RestAssured.delete("/api/comments/" + id)
                .then()
                .statusCode(204);
    }
}

