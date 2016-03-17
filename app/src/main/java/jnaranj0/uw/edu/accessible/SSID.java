package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

/**
 * Created by luisn on 3/17/2016.
 */
public class SSID extends SugarRecord {
    String ssid;
    //Location

    public SSID() {

    }

    public SSID(String ssid) {
        this.ssid = ssid;
    }

    public String toString() {
        return ssid + this.getId();
    }
}
