package in.resreq;

import dto.*;
import io.restassured.http.Cookies;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class ApiTests {

    @Test
    public void firstTest(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void secondTest(){

        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page",equalTo(2))
                .body("data[0].first_name",equalTo("Michael"))
                .body("total",notNullValue())
                .statusCode(200);
    }

    @Test
    public void thirdTest(){

        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page",equalTo(2))
                .body("data[0].first_name",equalTo("Michael"))
                .body("total",notNullValue())
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("page"),2,"Page is not 2");
    }

    @Test
    public void fourTest(){

        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page",equalTo(2))
                .body("data[0].first_name",equalTo("Michael"))
                .body("total",notNullValue())
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("page"),2,"Page is not 2");
    }

    @Test
    public void fiveTest(){
        ListUsers listUserInfo = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(ListUsers.class);
   //     Assert.assertTrue(listUserInfo.getData().stream().anyMatch(x-> x.getId() == 12));
    }

    @Test
    public void sixTest(){
        HashMap<String,String> reqLogin = new HashMap<>();
        reqLogin.put("email","eve.holt@reqres.in");
        reqLogin.put("password","cityslicka");
        given()
                .contentType("application/json")
                .body(reqLogin)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void sevenTest(){
        UserLogin userLogin = new UserLogin("eve.holt@reqres.in","cityslicka");
        given()
                .contentType("application/json")
                .body(userLogin)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void eightTest(){
        UserLogin userLogin = new UserLogin("eve.holt@reqres.in","cityslicka");
        Specification.installSpec(Specification.requestSpec(),Specification.responseSpec());
        given()
                .body(userLogin)
                .when()
                .post("/api/login")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void nineTest(){
        UserLogin userLogin = new UserLogin("eve.holt@reqres.in","cityslicka");
        Specification.installSpec(Specification.requestSpec(),Specification.responseSpec());
        Cookies cookies= given()
                .body(userLogin)
                .when()
                .post("/api/login")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response()
                .getDetailedCookies();

        given()
                .body(userLogin)
                .cookies(cookies)
                .when()
                .post("/api/login")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void tenTest(){
        given()
                .keyStore("src/Belyy.jks","123456")
                .when()
                .get("https://www.google.ru")
                .then()
                .log().all()
                .statusCode(200);
    }


    /**
     * @author Pruntsev Sergey
     */

    @Test
    public void firstTask(){
        AltListUsers altListUsers = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(AltListUsers.class);
        Assert.assertTrue(altListUsers.getData().stream().anyMatch(x->x.getYear()==2001));

        Assert.assertTrue(altListUsers.getData().size()==altListUsers.getPerPage());

        String regex ="[#][A-Z0-9]{6}";
        Assert.assertTrue(altListUsers.getData().stream().allMatch(x->x.getColor().matches(regex)));

    }
    @Test
    public void secondTask(){
        Specification.installSpec(Specification.requestSpec(),Specification.responseSpec());
        UserRegister userRegister = new UserRegister("eve.holt@reqres.in","pistol");
         AltUserInfo altUserInfo = given()
                 .body(userRegister)
                 .when()
                 .post("/api/register")
                 .then()
                 .log().body()
                 .statusCode(200)
                 .extract().as(AltUserInfo.class);
         Integer newUserId = altUserInfo.getId();

        given()
                .when()
                .get("/api/users/"+newUserId)
                .then()
                .log().body()
                .statusCode(200);
        given()
                .when()
                .delete("/api/users/"+newUserId)
                .then()
                .statusCode(204);

    }
    @Test
    public void thirdTask(){
        UserRegister userRegister = new UserRegister("eve.holt@reqres.in","");
        given()
                .body(userRegister)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().body()
                .statusCode(400);


    }


}
