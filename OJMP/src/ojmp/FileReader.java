package ojmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robbie Byrne robbiebyr@gmail.com
 */
public class FileReader {

    private static FileReader instance = null;

    private String path = "";
    private File file;
    private FileInputStream is;
    private final DbHelper helper = new DbHelper();

    protected FileReader() {
    }

    public void readFile() {
        setUpFileInputStream();
        int remainingBytes = 0;

        do {
            try {
                remainingBytes = is.available();
                startOfFile();
            } catch (IOException ex) {
                Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, "Can't read next byte", ex);
            }
        } while (remainingBytes != 0);
    }

    private void setUpFileInputStream() {
        path = "";
        file = new File(path);
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, "FileInputSteam can't be opened.", ex);
        }
    }

    private void startOfFile() {
        byte[] magicCookie = new byte[4];
        byte[] noOfFieldsInRecord = new byte[2];
        try {
            is.read(magicCookie);
            is.read(noOfFieldsInRecord);
        } catch (IOException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, "Can't read magic cookie from file.", ex);
        }

        helper.setMagicCookie(magicCookie);
        helper.setNoOfFieldsInRecord(noOfFieldsInRecord);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static FileReader getInstance() {
        if (instance == null) {
            instance = new FileReader();
        }
        return instance;
    }
}
