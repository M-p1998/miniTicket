package com.microservices.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//class UserServiceApplicationTests {
//
//    @ServiceConnection
//    static PostgreSQLContainer<?> postgres =
//            new PostgreSQLContainer<>("postgres:15")
//                    .withDatabaseName("user_service")
//                    .withUsername("root")
//                    .withPassword("bondstone");
//
//    static { postgres.start(); }
//
//    @LocalServerPort
//    Integer port;
//
//    @BeforeAll
//    static void init() {
//        RestAssured.baseURI = "http://localhost";
//    }
//
//    @BeforeEach
//    void setupPort() {
//        RestAssured.port = port;
//    }
//
//    @Test
//    void shouldCreateAndGetUser() {
//        String body = """
//        {
//          "username": "mya",
//          "email": "mya@test.com",
//          "title": "Analyst",
//          "funFacts": "Loves debugging and building microservices."
//        }
//        """;
//
//        var res = RestAssured.given()
//                .contentType("application/json")
//                .body(body)
//                .post("/api/users")
//                .then()
//                .statusCode(201)
//                .body("id", Matchers.notNullValue())
//                .body("username", Matchers.equalTo("mya"))
//                .body("title", Matchers.equalTo("Analyst"))
//                .extract();
//
//        Long id = ((Number) res.path("id")).longValue();
//
//        RestAssured.get("/api/users/" + id)
//                .then()
//                .statusCode(200)
//                .body("email", Matchers.equalTo("mya@test.com"));
//    }
//
//    @Test
//    void shouldUpdateUser() {
//        String create = """
//        {
//          "username": "sam",
//          "email": "sam@test.com",
//          "title": "Support Engineer",
//          "funFacts": "Coffee lover."
//        }
//        """;
//
//        var res = RestAssured.given()
//                .contentType("application/json")
//                .body(create)
//                .post("/api/users")
//                .then()
//                .statusCode(201)
//                .extract();
//
//        Long id = ((Number) res.path("id")).longValue();
//
//        String update = """
//        {
//          "email": "sam2@test.com",
//          "title": "Senior Support Engineer",
//          "funFacts": "Coffee lover and runner."
//        }
//        """;
//
//        RestAssured.given()
//                .contentType("application/json")
//                .body(update)
//                .put("/api/users/" + id)
//                .then()
//                .statusCode(200)
//                .body("email", Matchers.equalTo("sam2@test.com"))
//                .body("title", Matchers.equalTo("Senior Support Engineer"));
//    }
//
//    @Test
//    void shouldDeleteUser() {
//        String create = """
//        {
//          "username": "toDelete",
//          "email": "del@test.com",
//          "title": "Analyst",
//          "funFacts": "Temporary profile."
//        }
//        """;
//
//        var res = RestAssured.given()
//                .contentType("application/json")
//                .body(create)
//                .post("/api/users")
//                .then()
//                .statusCode(201)
//                .extract();
//
//        Long id = ((Number) res.path("id")).longValue();
//
//        RestAssured.delete("/api/users/" + id)
//                .then()
//                .statusCode(204);
//
//        RestAssured.get("/api/users/" + id)
//                .then()
//                .statusCode(404);
//    }
//    
//}



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserServiceApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("user_service")
                    .withUsername("root")
                    .withPassword("bondstone");

    @LocalServerPort
    int port;

    @BeforeAll
    static void init() {
        RestAssured.baseURI = "http://localhost";
    }

    @BeforeEach
    void setupPort() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateAndGetUser() {
        String body = """
        {
          "username": "bobby",
          "email": "bobby@test.com",
          "title": "Manager",
          "funFacts": "building things"
        }
        """;

        Long id = ((Number)
            RestAssured.given()
                .contentType("application/json")
                .body(body)
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
        ).longValue();

        RestAssured.get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("username", Matchers.equalTo("bobby"))
                .body("email", Matchers.equalTo("bobby@test.com"))
                .body("title", Matchers.equalTo("Manager"));
    }

    // ---------------- UPDATE ----------------
    @Test
    void shouldUpdateUser() {
        Long id = ((Number)
            RestAssured.given()
                .contentType("application/json")
                .body("""
                {
                  "username": "sam",
                  "email": "sam@test.com",
                  "title": "Analyst",
                  "funFacts": "debugging"
                }
                """)
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
        ).longValue();

        RestAssured.given()
            .contentType("application/json")
            .body("""
            {
              "username": "sam",
              "email": "sam.updated@test.com",
              "title": "Senior Analyst",
              "funFacts": "fixing bugs"
            }
            """)
            .put("/api/users/" + id)
            .then()
            .statusCode(200)
            .body("title", Matchers.equalTo("Senior Analyst"))
            .body("email", Matchers.equalTo("sam.updated@test.com"));
    }

    // ---------------- DELETE ----------------
    @Test
    void shouldDeleteUser() {
        Long id = ((Number)
            RestAssured.given()
                .contentType("application/json")
                .body("""
                {
                  "username": "deleteMe",
                  "email": "delete@test.com",
                  "title": "Temp",
                  "funFacts": "temporary"
                }
                """)
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
        ).longValue();

        RestAssured.delete("/api/users/" + id)
                .then()
                .statusCode(204);

        RestAssured.get("/api/users/" + id)
                .then()
                .statusCode(404);
    }
}

