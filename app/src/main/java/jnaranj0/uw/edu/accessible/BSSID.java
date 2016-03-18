package jnaranj0.uw.edu.accessible;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by luisn on 3/17/2016.
 */
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
}
