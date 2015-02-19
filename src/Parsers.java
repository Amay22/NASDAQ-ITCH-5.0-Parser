
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Parsers {

    private ParseDS parDS;
    // Constructor, takes a ParseDS object
    Parsers(ParseDS parDS) {
        this.parDS = parDS;
    }
    // Given a byte array and length, will return the length
    public String getString(byte[] payload, int len) throws UnsupportedEncodingException {
        // The length needs to be passed as well
        String str = "";
        for (int i = 0; i < payload.length; i++) {
            str = str + ByteBuffer.wrap(payload).getChar();
        }
        return new String(payload, "UTF8").replaceAll("\\W", "");
    }
    // given a byte array will return the Length of payload
    public Object getLen(byte[] payload) {
		// Convert to big end
        ByteBuffer length = ByteBuffer.wrap(payload);
        return (int) length.getShort(0);
    }
    // given byte array of size 4, will return an INT
    public String getInt(byte[] payload) {
        return Integer.toString(ByteBuffer.wrap(payload).getInt());
    }
    // given byte array of size 8, will return LONG
    public Object getLong(byte[] payload) {
        return Long.toString(ByteBuffer.wrap(payload).getLong());
    }
    // return char at beginning of byte array (Used to get messageType)
    public String getChar(byte[] payload) {

        String messageType = new String(payload);

        String c = "";
        c = c + messageType.charAt(0);

        return c;
    }
    // Payload in, get the messageType, the fields, then the get the message  
    public ArrayList<String> messageIn(byte[] payload) throws UnsupportedEncodingException {
        // Keep track of where we are at in the message
        int messagePointer = 0;
        ArrayList<String> messageArray = new ArrayList<String>();
        ArrayList<Object> fieldsArray = new ArrayList<Object>();
        // Get the messageType (getChar only returns A char in the first byte)
        String messageType = getChar(payload).toString();
        messageArray.add(messageType);
        // increment after look at first char
        messagePointer = messagePointer + 1;
        // Get the fields for this message
        fieldsArray = this.parDS.getFields(messageType);
        // loop over fields array, parsing messages --start at 1, we already have messageType
        for (int i = 1; i < fieldsArray.size(); i++) {
            // Get the field for this part of the message
            ArrayList<Object> fieldArray = this.parDS.getFormat((String) fieldsArray.get(i));
            // Call appropriate parser on the split payload
            messageArray.add(parse(Arrays.copyOfRange(payload, messagePointer,
                    messagePointer + ((Integer) fieldArray.get(1))),
                    fieldArray));
            // Move the array cursor after looking at this field
            messagePointer = messagePointer + ((Integer) fieldArray.get(1));
        }
        return messageArray;
    }
    // With input byteArray, len & parse type, call approp parser
    public String parse(byte[] arr, ArrayList<Object> fieldArray) throws UnsupportedEncodingException {
        String value = null;
        switch ((Integer) fieldArray.get(0)) {
            case 1:
                value = (String) getChar(arr);
                break;
            case 2:
                value = (String) getInt(arr);
                break;
            case 3:
                value = (String) getString(arr, (Integer) fieldArray.get(1));
                break;
            case 4:
                value = (String) getLong(arr);
                break;
        }
        return value;
    }

}
