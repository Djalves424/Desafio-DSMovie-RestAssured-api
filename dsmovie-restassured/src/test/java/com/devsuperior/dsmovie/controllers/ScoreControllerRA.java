package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScoreControllerRA {

    private String clientUsername, clientPassword;
    private String clientToken;

    private Long nonExistingScoreId;

    private Map<String, Object> putScoreInstance;

    @BeforeEach
    public void setUp() throws Exception {
        baseURI = "http://localhost:8080";

        clientUsername = "alex@gmail.com";
        clientPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);

        putScoreInstance = new HashMap<>();
        putScoreInstance.put("movieId", 1);
        putScoreInstance.put("score", 4);

        nonExistingScoreId = 100L;
    }

    @Test
    public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {

        JSONObject newScore = new JSONObject(putScoreInstance);

        given()
                .header("Content-type", "application/json").header("Authorization", "Bearer " + clientToken)
                .body(newScore).contentType(ContentType.JSON).accept(ContentType.JSON).when()
                .put("/scores/{movieId}", nonExistingScoreId).then().statusCode(404);
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {

        putScoreInstance.put("movieId", null);

        JSONObject newScore = new JSONObject(putScoreInstance);

        given()
                .header("Content-type", "application/json").header("Authorization", "Bearer " + clientToken)
                .body(newScore).contentType(ContentType.JSON).accept(ContentType.JSON).when()
                .put("/scores").then().statusCode(422).body("errors.message[0]", equalTo("Campo requerido"));
    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {

        putScoreInstance.put("score", -8);

        JSONObject newScore = new JSONObject(putScoreInstance);

        given()
                .header("Content-type", "application/json").header("Authorization", "Bearer " + clientToken)
                .body(newScore).contentType(ContentType.JSON).accept(ContentType.JSON).when()
                .put("/scores").then().statusCode(422).body("errors.message[0]", equalTo("Valor mínimo 0"));
    }
}
