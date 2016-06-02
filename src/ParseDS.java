
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public final class ParseDS {
    // yaml DS map
    private final Map<Object, Object> yMap;
    private Map<Object, Object> fMap;
    private Map<Object, Object> mMap;
    private final Yaml yaml;
    // constructor will take in the yaml parser
    ParseDS(String filename) throws FileNotFoundException {
        yaml = new Yaml();
        InputStream yInput;
        yInput = new FileInputStream(new File(filename));
        // load the yaml data structure
        Object y = yaml.load(yInput);
        // convert the data structure to a map
        yMap = (Map<Object, Object>) y;
        buildFormats();
        buildMessages();
    }
    // Build the formats data structure
    public void buildFormats() {
        fMap = new HashMap<>();
        ArrayList<Object> fArray = (ArrayList<Object>) yMap.get("formats");

	for (int i = 0; i < fArray.size(); ++i) {
	    Map<Object,Object> tempMap = (Map<Object,Object>) (fArray.get(i));
	    fMap.put(tempMap.keySet().toArray()[0],
		     tempMap.values().toArray()[0]);
	}

        /*fArray.stream().map((fArray1) -> (Map<Object, Object>) fArray1).forEach((tempMap) -> {
            fMap.put(tempMap.keySet().toArray()[0], tempMap.values().toArray()[0]);
	    });*/
    }
    // Build the messages data structure
    public void buildMessages() {
        mMap = (Map<Object, Object>) yMap.get("messages");
    }
    // Given Message type, return an array of the fields
    public ArrayList<Object> getFields(String mType) {
        Map<Object, Object> tempMap = (Map<Object, Object>) mMap.get(mType);
        return (ArrayList<Object>) tempMap.get("fields");
    }
    // Given the field, get the correct parser format for that field
    public ArrayList<Object> getFormat(String field) {
        return (ArrayList<Object>) fMap.get(field);
    }
    // Given message type, return String Message Name
    public String getFieldName(String mType) {
        Map<Object, Object> tempMap = (Map<Object, Object>) mMap.get(mType);
        return tempMap.get("name").toString();
    }

}
