package utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// JsonUtils provides a reusable method to read any JSON file under src/test/resources
public class JsonUtils {

    public static JsonNode readPageJson(String filePath, String pageKey) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new FileReader(filePath));
            return root.get(pageKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
