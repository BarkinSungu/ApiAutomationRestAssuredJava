package Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class CommonLib {

    public CommonLib() {

    }

    //The function fetch data from dataset json file
    public Object getDataFromDatasetJson(String key){
        Object object = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Read JSON file
            JsonNode rootNode = mapper.readTree(new File("src/test/java/Common/dataset.json"));

            // Get object
            JsonNode pagesNode = rootNode.path(key);
            object = getNodeValue(pagesNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    // Dynamically fetch the value inside JsonNode
    private static Object getNodeValue(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isArray()) {
            StringBuilder arrayValues = new StringBuilder("[");
            node.forEach(element -> arrayValues.append(element.asText()).append(", "));
            arrayValues.setLength(arrayValues.length() - 2); // Remove the last comma and space
            arrayValues.append("]");
            return arrayValues.toString();
        } else if (node.isObject()) {
            // Return as a JSON string for object values
            return node.toString();
        }
        return null;
    }
}
