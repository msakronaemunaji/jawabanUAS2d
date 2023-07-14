import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonDataExample {
    public static void main(String[] args) {
        // URL server
        String serverUrl = "https://dummyjson.com/products/search?q=Laptop";

        // Membaca data JSON dari server
        List<Product> data = readJsonDataFromServer(serverUrl);

        // Menampilkan data
        for (Product product : data) {
            System.out.println(product);
        }
    }

    // Metode untuk membaca data JSON dari server
    public static List<Product> readJsonDataFromServer(String serverUrl) {
        List<Product> data = new ArrayList<>();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(serverUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity);

                JsonFactory factory = new JsonFactory();
                JsonParser parser = factory.createParser(jsonString);

                // Melakukan parsing JSON
                while (parser.nextToken() == JsonToken.START_OBJECT) {
                    Product product = new Product();
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = parser.getCurrentName();
                        parser.nextToken();
                        if ("name".equals(fieldName)) {
                            product.setName(parser.getText());
                        } else if ("price".equals(fieldName)) {
                            product.setPrice(parser.getIntValue());
                        } else if ("rating".equals(fieldName)) {
                            product.setRating(parser.getDoubleValue());
                        }
                    }
                    data.add(product);
                }

                parser.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}

