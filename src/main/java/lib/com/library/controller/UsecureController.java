package lib.com.library.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/usecure")
public class UsecureController {

    private final OkHttpClient client = new OkHttpClient();
    private final String uri = "https://jsonplaceholder.typicode.com/posts";
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping({ "", "/", "/hello" })
    public ResponseEntity<?> hello() {
        Request request = new Request.Builder().url(uri).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return ResponseEntity.status(response.code())
                        .body(Map.of("error", "External API error: HTTP " + response.code()));
            }

            if (response.body() == null) {
                return ResponseEntity.ok(Map.of("response", "Empty body"));
            }

            String responseBody = response.body().string();
            Object parsed = mapper.readValue(responseBody, Object.class);

            return ResponseEntity.ok(Map.of("response", parsed));
        } catch (Exception e) {
            System.err.println("[UsecureController] Error fetching posts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch external data: " + e.getMessage()));
        }
    }
    //  @GetMapping("/getbooks")
    // String getBookList(String hello){
    //     hello = "Okay Successfully";
    //     return hello;
    // }
}
