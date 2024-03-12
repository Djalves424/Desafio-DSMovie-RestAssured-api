package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String clientToken, adminToken, invalidToken;
    private Long existingMovieId, nonExistingMovieId;
    private String titleMovie;

    private Map<String, Object> putMovieInstance;

    @BeforeEach
    public void setUp() throws Exception {
        baseURI = "http://localhost:8080";

        clientUsername = "alex@gmail.com";
        clientPassword = "123456";
        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "xpto";

        titleMovie = "The Witcher";

        putMovieInstance = new HashMap<>();
        putMovieInstance.put("title", "Test Movie");
        putMovieInstance.put("score", 0.0);
        putMovieInstance.put("count", 0);
        putMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");

    }

    @Test
    public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

        given().get("/movies").then().statusCode(200)
                .body("content.title", hasItem("The Witcher"));
    }

    @Test
    public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {

        given().get("/movies?page=0&name={titleMovie}", titleMovie).then().statusCode(200)
                .body("content.title[0]", equalTo("The Witcher"));
    }

    @Test
    public void findByIdShouldReturnMovieWhenIdExists() {

        existingMovieId = 1L;

        given().get("/movies/{id}", existingMovieId).then().statusCode(200)
                .body("id", is(1));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {

        nonExistingMovieId = 100L;

        given().get("/movies/{id}", nonExistingMovieId).then().statusCode(404);
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {

        putMovieInstance.put("title", " ");

        JSONObject newMovie = new JSONObject(putMovieInstance);

        given()
                .header("Content-type", "application/json").header("Authorization", "Bearer " + adminToken)
                .body(newMovie).contentType(ContentType.JSON).accept(ContentType.JSON).when().post("/movies")
                .then().statusCode(422);
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

        JSONObject newMovie = new JSONObject(putMovieInstance);

        given()
                .header("Content-type", "application/json").header("Authorization", "Bearer " + clientToken)
                .body(newMovie).contentType(ContentType.JSON).accept(ContentType.JSON).when().post("/movies")
                .then().statusCode(403);
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

        JSONObject newMovie = new JSONObject(putMovieInstance);

        given()
                .header("Content-type", "application/json").header("Authorization", "Bearer " + invalidToken)
                .body(newMovie).contentType(ContentType.JSON).accept(ContentType.JSON).when().post("/movies")
                .then().statusCode(401);
    }
}
