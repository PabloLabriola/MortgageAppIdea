package utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ParamConfig.paramConfig;

public class RestAssuredExtension {

    public static RequestSpecification request;

    public RestAssuredExtension(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(Constants.GATEWAY_HOST);
        builder.setContentType(ContentType.JSON);
        var requestSpec = builder.build();
        request = RestAssured.given().spec(requestSpec);
    }

    public static void getOperation(String url) throws URISyntaxException {
        request.get(new URI(url));
    }

    public static ResponseOptions<Response> postOperationWithBody(String url, Map<String, String> body) {
        request.body(body);
        try {
            return request.post(new URI(url));
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public static ResponseOptions<Response> postOperationWithHeaderAndBody(String url, Map<String, String> headers, Map<String, String> body) {
        request.body(body);
        request.headers(headers);
        try {
            return request.post(new URI(url));
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public static ResponseOptions<Response> postOperationWithParamsAndHeaders(String url, Map<String, String> headers, String key, Object value) {
        request.config(config().paramConfig(paramConfig().replaceAllParameters()));
        request.headers(headers);
        request.queryParam(key, value);
        try {
            return request.post(new URI(url));
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
}
