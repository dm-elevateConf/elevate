import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteObjectsTests {

    @Test
    public void successfullyDeletePreviouslyCreatedObjectTest() {

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

        Response responseToObjectPatch =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        when().
                        delete(Endpoints.baseUrl + Endpoints.objects + "/" + objectId);
        assertEquals(responseToObjectPatch.statusCode(), HttpStatus.OK_200);

    }

    @Test
    public void checkErrorMessageWhenPerformingDeleteOnNonExistingObjectTest() {

        Response responseToObjectPatch =
                given().log().all().
                        accept(ContentType.JSON).
                        contentType(ContentType.JSON).
                        when().
                        delete(Endpoints.baseUrl + Endpoints.objects + "/" + "876543");
        assertEquals(responseToObjectPatch.statusCode(), HttpStatus.NOT_FOUND_404);
        JsonPath responseContentsOfPatch = new JsonPath(responseToObjectPatch.getBody().prettyPrint());
        assertEquals("Object with id = 876543 doesn't exist.", responseContentsOfPatch.get("error"));

    }
}
