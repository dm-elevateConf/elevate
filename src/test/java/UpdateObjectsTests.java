import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateObjectsTests {

    @Test
    public void successfullyUpdatePreviouslyCreatedObjectTest() {

        Data dd = new Data(2025, 2499.99, "i9", "1 TB");
        NewDevice nd = new NewDevice("Dell", dd);

        Response responseToObjectCreation =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(nd).
                        when().
                        post(Endpoints.baseUrl + Endpoints.objects);
        assertEquals(responseToObjectCreation.statusCode(), HttpStatus.OK_200);

        JsonPath responseContents = new JsonPath(responseToObjectCreation.getBody().prettyPrint());
        String objectId = responseContents.get("id");

        Data updateDd = new Data(2023, 1500.0, "i5", "500 GB");
        NewDevice updateNd = new NewDevice("Lenovo", updateDd);

        Response responseToObjectPut =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(updateNd).
                        when().
                        put(Endpoints.baseUrl + Endpoints.objects + "/" + objectId);
        
        assertEquals(responseToObjectPut.statusCode(), HttpStatus.OK_200);
        JsonPath responseContentsOfPut = new JsonPath(responseToObjectPut.getBody().prettyPrint());
        assertEquals("Lenovo", responseContentsOfPut.get("name"));
        assertEquals("2023", responseContentsOfPut.get("data.year").toString());
        assertEquals("1500.0", responseContentsOfPut.get("data.price").toString());
        assertEquals("i5", responseContentsOfPut.get("data.cpu"));
        assertEquals("500 GB", responseContentsOfPut.get("data.hdd_Size"));
    }

    @Test
    public void checkErrorMessageWhenPerformingAnUpdateOnNonExistingObjectTest() {

        Map<String, String> data = new HashMap<>();
        data.put("name", "Lenovo");

        Response responseToObjectUpdate =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(data).
                        when().
                        put(Endpoints.baseUrl + Endpoints.objects + "/" + "876543");
        assertEquals(responseToObjectUpdate.statusCode(), HttpStatus.NOT_FOUND_404);
        JsonPath responseContentsOfPatch = new JsonPath(responseToObjectUpdate.getBody().prettyPrint());
        assertEquals("The Object with id = 876543 doesn't exist. Please provide an object id which exists or generate a new Object using POST request and capture the id of it to use it as part of PUT request after that.", responseContentsOfPatch.get("error"));

    }
}
