import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateObjectTests {
    @Test
    public void successfullyCreateNewObjectTest() {

        Data dd = new Data(2025, 2499.99, "i9", "1 TB");
        NewDevice nd = new NewDevice("Dell", dd);

        Response response =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(nd).
                        when().
                        post(Endpoints.baseUrl + Endpoints.objects);
        assertEquals(response.statusCode(), HttpStatus.OK_200);

        JsonPath responseContents = new JsonPath(response.getBody().prettyPrint());
        assertEquals("Dell", responseContents.get("name"));
        assertEquals("i9", responseContents.get("data.cpu"));
        assertEquals("1 TB", responseContents.get("data.hdd_Size"));

    }

    @Test
    public void checkErrorMessageWhenBodyIsNotProvidedToCreateNewObjectTest() {

        Response response =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        when().
                        post(Endpoints.baseUrl + Endpoints.objects);
        assertEquals(response.statusCode(), HttpStatus.BAD_REQUEST_400);

        JsonPath responseContents = new JsonPath(response.getBody().prettyPrint());
        assertEquals("400 Bad Request. If you are trying to create or update the data, potential issue is that you are sending incorrect body json or it is missing at all.", responseContents.get("error"));

    }

    @Test
    public void checkErrorMessageWhenProvidingIdToCreateNewObjectTest() {

        Data dd = new Data(2025, 2499.99, "i9", "1 TB");
        NewDevice nd = new NewDevice("Dell", dd);

        Response response =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(nd).
                        when().
                        post(Endpoints.baseUrl + Endpoints.objects + "/22");
        assertEquals(response.statusCode(), HttpStatus.METHOD_NOT_ALLOWED_405);

        JsonPath responseContents = new JsonPath(response.getBody().prettyPrint());
        assertEquals("Method Not Allowed", responseContents.get("error"));

    }
}
