package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.dto.CreateUserRequest;
import io.github.robinhosz.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource("/user")
    URL apiUrl;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("should create an user successfully")
    @Order(1)
    void createUserTest() {
        var user = new CreateUserRequest();
        user.setAge(30);
        user.setName("Robson");

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                        .when()
                        .post(apiUrl)
                        .then()
                        .extract().response();

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));

    }

    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    void createUserValidationTest() {
        var user = new CreateUserRequest();
        user.setAge(null);
        user.setName(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiUrl)
                .then()
                .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.getStatusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
        assertEquals("Age is Required", errors.get(0).get("message"));
        assertEquals("Name is Required", errors.get(1).get("message"));

    }

    @Test
    @DisplayName("should list all users")
    @Order(3)
    void findAllUsers() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl)
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }

}