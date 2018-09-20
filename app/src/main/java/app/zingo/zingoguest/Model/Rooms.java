package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class Rooms implements Serializable {

    @SerializedName("RoomId")
    private int RoomId;

    @SerializedName("LongDescription")
    private String LongDescription;

    @SerializedName("RoomCategoryId")
    private int RoomCategoryId;

    @SerializedName("DisplayName")
    private String DisplayName;

    @SerializedName("RoomSizeInLength")
    private String RoomSizeInLength;

    @SerializedName("RoomSizeInBreadth")
    private String RoomSizeInBreadth;

    @SerializedName("TotalRooms")
    private String TotalRooms;

    @SerializedName("BedTypeId")
    private int BedTypeId;

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("RoomOccupancyId")
    private int RoomOccupancyId;

    @SerializedName("Floor")
    private String Floor;

    @SerializedName("RoomNo")
    private String RoomNo;

    @SerializedName("Status")
    private String Status;

    @SerializedName("DisplayRate")
    private String DisplayRate;

    @SerializedName("SellRate")
    private String SellRate;

    @SerializedName("ExtraRate")
    private String ExtraRate;

    @SerializedName("DiscountPer")
    private String DiscountPer;

    @SerializedName("GstPer")
    private String GstPer;

    @SerializedName("GstAmnt")
    private int GstAmnt;

    @SerializedName("BlockingReason")
    private String blockingReason;

    @SerializedName("HourlyCharges")
    private int HourlyCharges;

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public int getRoomCategoryId() {
        return RoomCategoryId;
    }

    public void setRoomCategoryId(int roomCategoryId) {
        RoomCategoryId = roomCategoryId;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getRoomSizeInLength() {
        return RoomSizeInLength;
    }

    public void setRoomSizeInLength(String roomSizeInLength) {
        RoomSizeInLength = roomSizeInLength;
    }

    public String getRoomSizeInBreadth() {
        return RoomSizeInBreadth;
    }

    public void setRoomSizeInBreadth(String roomSizeInBreadth) {
        RoomSizeInBreadth = roomSizeInBreadth;
    }

    public String getTotalRooms() {
        return TotalRooms;
    }

    public void setTotalRooms(String totalRooms) {
        TotalRooms = totalRooms;
    }

    public int getBedTypeId() {
        return BedTypeId;
    }

    public void setBedTypeId(int bedTypeId) {
        BedTypeId = bedTypeId;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public int getRoomOccupancyId() {
        return RoomOccupancyId;
    }

    public void setRoomOccupancyId(int roomOccupancyId) {
        RoomOccupancyId = roomOccupancyId;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDisplayRate() {
        return DisplayRate;
    }

    public void setDisplayRate(String displayRate) {
        DisplayRate = displayRate;
    }

    public String getSellRate() {
        return SellRate;
    }

    public void setSellRate(String sellRate) {
        SellRate = sellRate;
    }

    public String getExtraRate() {
        return ExtraRate;
    }

    public void setExtraRate(String extraRate) {
        ExtraRate = extraRate;
    }

    public String getDiscountPer() {
        return DiscountPer;
    }

    public void setDiscountPer(String discountPer) {
        DiscountPer = discountPer;
    }

    public String getGstPer() {
        return GstPer;
    }

    public void setGstPer(String gstPer) {
        GstPer = gstPer;
    }

    public int getGstAmnt() {
        return GstAmnt;
    }

    public void setGstAmnt(int gstAmnt) {
        GstAmnt = gstAmnt;
    }

    public String getBlockingReason() {
        return blockingReason;
    }

    public void setBlockingReason(String blockingReason) {
        this.blockingReason = blockingReason;
    }

    public int getHourlyCharges() {
        return HourlyCharges;
    }

    public void setHourlyCharges(int hourlyCharges) {
        HourlyCharges = hourlyCharges;
    }
}
