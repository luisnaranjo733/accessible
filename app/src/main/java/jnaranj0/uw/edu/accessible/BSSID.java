package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

import java.util.List;

public class BSSID extends SugarRecord {
    String nickname;
    String bssid;
    SSID ssid;

    public BSSID() {

    }

    public BSSID(String nickname, String bssid, SSID ssid) {
        this.nickname = nickname;
        this.bssid = bssid;
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
}
