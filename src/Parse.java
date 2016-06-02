import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Parse {
    private String filename;
    private boolean parseNPrint;
    private byte[] lenBytes = new byte[2];
    private InputStream input;
    Parsers parsers;
    ParseDS parseDS;

    public Parse(String filename, String yamlFile, boolean parseNPrint) {
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
        // Open the input stream...
        try {
	    if (filename.length() > 0) {
		// From the file's path if specified...
		input = new FileInputStream(new File(filename));
	    } else {
		/// ...or stdin otherwise
		input = System.in;
	    }
        } catch (FileNotFoundException e) {
            System.out.println("File (" + filename +
			       ") not found...Check Filename");
        }
    }

    // Read the message from binary file
    public byte[] parse() throws IOException, InterruptedException {
        if (input.read() == -1) { // EOF
            return null;
        }
        int payLength = input.read();
        byte[] payBytes = new byte[payLength]; // Get the payload

	int offset = 0;  // Loop until we've read the full payload size
	while (offset < payLength) {
	    offset += input.read(payBytes, offset, (payBytes - offset));
	}

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

    public static boolean hasCmdArgsYamlFlag (String args[]) {
	return args.length >= 1 && args[0].equals("-y");
    }

    public static String readYamlPathArgs (String args[]) {
	if (hasCmdArgsYamlFlag(args)) {
	    return args[1];
	} else {
	    return "..//itch5.yaml";
	}
    }

    public static String readItchPathArgs (String args[]) {
	int argsPos = hasCmdArgsYamlFlag(args) ? 2 : 0;
	if (args.length > argsPos) {
	    return args[argsPos];
	} else {
	    return "";
	}
    }

    public static void main(String args[]) throws IOException, InterruptedException {
	String yamlPath = readYamlPathArgs(args);
        String filename = readItchPathArgs(args);
        Parse parse = new Parse(filename, yamlPath,
				true); // true to print otherwise enter false
        while (parse.parse() != null) {}
    }
}
