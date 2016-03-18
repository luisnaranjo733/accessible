package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

import java.util.List;

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

    // Get all BSSIDs from this SSID
    List<BSSID> getBSSIDs() {
        return BSSID.find(BSSID.class, "ssid = ?", "" + getId());
    }

    public int getNodes() {
        List<BSSID> results = getBSSIDs();
        return results.size();
    }
}
