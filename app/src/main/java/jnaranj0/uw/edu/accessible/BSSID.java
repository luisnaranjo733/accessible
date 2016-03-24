package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class BSSID extends SugarRecord {
    String nickname;
    String bssid;
    int frequency;
    SSID ssid;

    public BSSID() {

    }

    public BSSID(String nickname, String bssid, int frequency, SSID ssid) {
        this.nickname = nickname;
        this.bssid = bssid;
        this.frequency = frequency;
        this.ssid = ssid;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BSSID) {
            BSSID other = (BSSID) o;
            return other.bssid.equals(this.bssid);
        } else {
            return false;
        }
    }

    public int getChannel() {
        if (frequency >= 2412 && frequency <= 2484) {
            return (frequency - 2412) / 5 + 1;
        } else if (frequency >= 5170 && frequency <= 5825) {
            return (frequency - 5170) / 5 + 34;
        } else {
            return -1;
        }
    }

    public String getBand() {
        if (frequency > 2400 && frequency < 2500) {
            return "2.4 ghz";
        } else if (frequency >= 5180 && frequency <= 5825) {
            return "5 ghz";
        } else {
            return "" + frequency + " mhz";
        }
    }

    public static ArrayList<Long> serialize(ArrayList<BSSID> bssids) {
        ArrayList<Long> serialized = new ArrayList<>();
        if (bssids != null) {
            for (BSSID bssid : bssids) {
                serialized.add(bssid.getId());
            }
        }
        return serialized;
    }

    public static ArrayList<BSSID> unserialize(ArrayList<Long> primaryKeys){
        ArrayList<BSSID> unserialized = new ArrayList<>();
        for (long pk : primaryKeys) {
            List<BSSID> results = BSSID.find(BSSID.class, "id = ?", "" + pk);
            BSSID bssid = results.get(0);
            unserialized.add(bssid);
        }
        return unserialized;
    }
}
