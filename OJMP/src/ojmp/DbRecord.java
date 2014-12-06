package ojmp;

import java.util.Date;

/**
 *
 * @author Robbie Byrne
 */
public class DbRecord {

    String name;
    String location;
    int size;
    boolean smoking;
    String rate;
    Date date;
    String customerId;

    public String getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = new String(name);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(byte[] location) {
        this.location = new String(location);
    }

    public int getSize() {
        return size;
    }

    public void setSize(byte[] size) {
        this.size = new Integer(new String(size));
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(byte[] smoking) {
        this.smoking = new Boolean(new String(smoking));
    }

    public String getRate() {
        return rate;
    }

    public void setRate(byte[] rate) {
        this.rate = new String(rate);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(byte[] date) {
        this.date = new Date(new String(date));
    }

    public String getOwner() {
        return customerId;
    }

    public void setOwner(byte[] owner) {
        this.customerId = new String(owner);
    }

}
