import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatchObjectsTests {

    @Test
    public void successfullyPatchPreviouslyCreatedObjectTest() {

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

        Map<String, String> data = new HashMap<>();
        data.put("name", "Lenovo");

        Response responseToObjectPatch =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(data).
                        when().
                        patch(Endpoints.baseUrl + Endpoints.objects + "/" + objectId);
        assertEquals(responseToObjectPatch.statusCode(), HttpStatus.OK_200);
        JsonPath responseContentsOfPatch = new JsonPath(responseToObjectPatch.getBody().prettyPrint());
        assertEquals("Lenovo", responseContentsOfPatch.get("name"));
        assertEquals("i9", responseContentsOfPatch.get("data.cpu"));
        assertEquals("1 TB", responseContentsOfPatch.get("data.hdd_Size"));
    }

    @Test
    public void checkErrorMessageWhenPerformingPatchOnNonExistingObjectTest() {

        Map<String, String> data = new HashMap<>();
        data.put("name", "Lenovo");

        Response responseToObjectPatch =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        body(data).
                        when().
                        patch(Endpoints.baseUrl + Endpoints.objects + "/" + "876543");
        assertEquals(responseToObjectPatch.statusCode(), HttpStatus.NOT_FOUND_404);
        JsonPath responseContentsOfPatch = new JsonPath(responseToObjectPatch.getBody().prettyPrint());
        assertEquals("The Object with id = 876543 doesn't exist. Please provide an object id which exists or generate a new Object using POST request and capture the id of it to use it as part of PATCH request after that.", responseContentsOfPatch.get("error"));

    }
}
