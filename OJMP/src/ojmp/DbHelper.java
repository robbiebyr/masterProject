package ojmp;

/**
 *
 * @author Robbie Byrne
 */
public class DbHelper {
    
    private String magicCookie;
    private int noOfFieldsInRecord;

    public String getMagicCookie() {
        return magicCookie;
    }

    public void setMagicCookie(byte[] magicCookie) {
        this.magicCookie = new String(magicCookie);
    }

    public int getNoOfFieldsInRecord() {
        return noOfFieldsInRecord;
    }

    public void setNoOfFieldsInRecord(byte[] noOfFieldsInRecord) {
        this.noOfFieldsInRecord = new Integer(new String(noOfFieldsInRecord));
    }
    
    
    
}
