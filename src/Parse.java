import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Parse {
    private String filename;    
    private String yamlFile = "itch5.yaml";
    private boolean parseNPrint;
    private byte[] lenBytes = new byte[2];
    private InputStream input;
    Parsers parsers;
    ParseDS parseDS;
    public Parse(String filename, boolean parseNPrint) {
        // Check arg for printing parsing the message 
        this.parseNPrint = parseNPrint;
        // Via YAML, create the Data Structures 
        try {
            parseDS = new ParseDS(yamlFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found...Check Filename");
        }
        // Create the parsers with the YAML data structures
        parsers = new Parsers(parseDS);
        this.filename = filename;
        // Open the data file
        try {
            input = new FileInputStream(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File not found...Check Filename");
        }
    }

    // Read the message from binary file
    public byte[] parse() throws IOException, InterruptedException {      
        if (input.read() == -1) { // EOF
            return null;
        }
        int payLength = input.read();        
        byte[] payBytes = new byte[payLength]; // Get the payload
        input.read(payBytes);

        // Check if we are parsing and printing
        if (parseNPrint) {
            ArrayList<String> messageArr = null;
            // Send payBytes byte[] to Parsers for processing
            // An arraylist will be returned with the message
            messageArr = (parsers.messageIn(payBytes));
            System.out.println(messageArr);
        }
        return payBytes;
    }    
    public static void main(String args[]) throws IOException, InterruptedException {        
        String filename = "C:\\Users\\Amay\\Documents\\NetBeansProjects\\ItchParser\\06022014.NASDAQ_ITCH50";
        // true to print
        Boolean PP = true;
        Parse parse = new Parse(filename, PP);
        /*int count = 0;
        final long startTime = System.currentTimeMillis();*/
        while (parse.parse() != null) {}
        /*final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) * 0.001);
        System.out.println("Messages/second: " + count / ((endTime - startTime) * 0.001));*/

    }
}
