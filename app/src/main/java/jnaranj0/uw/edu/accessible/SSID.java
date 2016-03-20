package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

import java.util.List;

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof SSID) {
            SSID other = (SSID) o;
            return other.ssid.equals(this.ssid) && other.getId().equals(this.getId());
        } else {
            return false;
        }
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
