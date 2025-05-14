import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetObjectsTests {


    @Test
    public void successfullyGetAllObjects() {
        Response response =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        when().
                        get(Endpoints.baseUrl + Endpoints.objects);
        assertEquals(response.statusCode(), HttpStatus.OK_200);
        assertTrue(response.getHeaders().hasHeaderWithName("Content-Type"));
    }

    @Test
    public void successfullyGetMultipleObjectsById() {
        Response response =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        queryParam("id", "3").
                        queryParam("id", "7").
                        when().
                        get(Endpoints.baseUrl + Endpoints.objects);
        assertEquals(response.statusCode(), HttpStatus.OK_200);

        JsonPath responseContents = new JsonPath(response.getBody().prettyPrint());

        assertEquals("3", responseContents.get("id[0]"));
        assertEquals("Apple iPhone 12 Pro Max", responseContents.get("name[0]"));
        assertEquals("7", responseContents.get("id[1]"));
        assertEquals("Apple MacBook Pro 16", responseContents.get("name[1]"));
    }

    @Test
    public void successfullyGetObjectById() {
        Response response =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        when().
                        get(Endpoints.baseUrl + Endpoints.objects + "/5");

        assertEquals(HttpStatus.OK_200, response.statusCode());

        JsonPath responseContents = new JsonPath(response.getBody().prettyPrint());
        assertEquals("5", responseContents.get("id"));
        assertEquals("Samsung Galaxy Z Fold2", responseContents.get("name"));
        assertEquals("Brown", responseContents.get("data.color"));
        assertEquals("689.99", responseContents.get("data.price").toString());
    }

}
