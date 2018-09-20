package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class TravellerDeviceMapping implements Serializable {

    @SerializedName("TravellerDeviceMappingId")
    private int TravellerDeviceMappingId;

    @SerializedName("TravellerId")
    private int TravellerId;

    @SerializedName("DeviceId")
    private String DeviceId;

    public int getTravellerDeviceMappingId() {
        return TravellerDeviceMappingId;
    }

    public void setTravellerDeviceMappingId(int travellerDeviceMappingId) {
        TravellerDeviceMappingId = travellerDeviceMappingId;
    }

    public int getTravellerId() {
        return TravellerId;
    }

    public void setTravellerId(int travellerId) {
        TravellerId = travellerId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
}
