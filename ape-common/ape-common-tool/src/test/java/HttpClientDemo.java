import com.airport.ape.tool.HttpClientUtils;
import javafx.util.Pair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientDemo {

    public static void main(String[] args) {
        // Example API endpoint
        String apiUrl = "http://localhost:8081/logFile";

        // Example request parameters
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("path", "C:/Users/lee/Desktop"));
        params.add(new BasicNameValuePair("waybill", "99989093034"));
        params.add(new BasicNameValuePair("type", "2"));

        // Example request headers
        Map<String, String> headers = new HashMap<>();
        //headers.put("Content-Type", "application/x-www-form-urlencoded");
        //headers.put("Authorization", "Bearer YourAccessToken");

        // Make the HTTP POST request using HttpClientUtils
        String response = HttpClientUtils.get(apiUrl, params, headers);

        // Process the response
        System.out.println("Response from the server: " + response);
    }

}