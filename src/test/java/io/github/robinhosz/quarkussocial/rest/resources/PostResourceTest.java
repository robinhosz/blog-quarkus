package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.dto.CreatePostRequest;
import io.github.robinhosz.quarkussocial.rest.entities.User;
import io.github.robinhosz.quarkussocial.rest.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository repository;
    Long userId;

    Long userNotFollowerId;

    @BeforeEach
    @Transactional
    public void setUp() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        repository.persist(user);
        userId = user.getId();

        var userNotFollower = new User();
        userNotFollower.setAge(30);
        userNotFollower.setName("Cicrano");

        repository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

    }

    @Test
    @DisplayName("should create a post for user")
    void createPostTest() {

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);

    }

    @Test
    @DisplayName("should return 404 when trying to make a a post for an inexistent user")
    void postForInexistentUserTest() {

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUserId)
                .when()
                .post()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("should return 404 when user doesn't exist")
    void listPostUserNotFoundTest() {
        var inexistentUserId = 999;

        given()
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when followerId header is not present")
    void listPostFollowerHeaderNotSendTest() {

        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));

    }

    @Test
    @DisplayName("should return 400 when follower doesn't exist")
    void listPostFollowerNotFoundTest() {

        var inexistentFollowerId = 999;

        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Inexistent followerId"));

    }

    @Test
    @DisplayName("should return 405 when follower isn't a follower")
    void listPostNotFollower() {

        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));

    }

    @Test
    @DisplayName("should return posts")
    void listPostsTests() {

    }

}