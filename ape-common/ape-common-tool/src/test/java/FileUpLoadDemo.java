import com.airport.ape.tool.HttpClientUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUpLoadDemo {
    public static void main(String[] args) {
        // Example API endpoint
        String apiUrl = "http://localhost:8081/upload";

        // Example request parameters
        Map<String,Object> map = new HashMap<>();
        map.put("parent","D:\\database\\test");
        // Example request headers
        Map<String, String> headers = new HashMap<>();
        //headers.put("Content-Type", "application/x-www-form-urlencoded");
        //headers.put("Authorization", "Bearer YourAccessToken");

        // Make the HTTP POST request using HttpClientUtils
        String response = HttpClientUtils.multipleFilePost(apiUrl, map, headers);

        // Process the response
        System.out.println("Response from the server: " + response);
    }
}
