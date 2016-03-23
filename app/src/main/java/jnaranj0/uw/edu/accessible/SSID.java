package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SSID extends SugarRecord {
    String ssid;
    //Location

    public SSID() {}

    public SSID(String ssid) {
        this.ssid = ssid;
    }

    public String toString() {
        return ssid;
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
    ArrayList<BSSID> getBSSIDs() {
        ArrayList<BSSID> arrayList = new ArrayList<>();
        for (BSSID bssid: BSSID.find(BSSID.class, "ssid = ?", "" + getId())) {
            arrayList.add(bssid);
        }
        return arrayList;
    }

    public static ArrayList<Long> serialize(ArrayList<SSID> ssids) {
        ArrayList<Long> serialized = new ArrayList<>();
        if (ssids != null) {
            for (SSID ssid : ssids) {
                serialized.add(ssid.getId());
            }
        }
        return serialized;
    }

    public static ArrayList<SSID> unserialize(ArrayList<Long> primaryKeys){
        ArrayList<SSID> unserialized = new ArrayList<>();
        for (long pk : primaryKeys) {
            List<SSID> results = SSID.find(SSID.class, "id = ?", "" + pk);
            SSID ssid = results.get(0);
            unserialized.add(ssid);
        }
        return unserialized;
    }
}
