package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class RoomCategories {

    @SerializedName("RoomCategoryId")
    private int RoomCategoryId;

    @SerializedName("CategoryName")
    private String CategoryName;

    @SerializedName("Description")
    private String Description;

    @SerializedName("ratePlan")
    private RatePlans ratePlan;

    @SerializedName("HotelsId")
    private int HotelsId;

    public int getRoomCategoryId() {
        return RoomCategoryId;
    }

    public void setRoomCategoryId(int roomCategoryId) {
        RoomCategoryId = roomCategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public RatePlans getRatePlan() {
        return ratePlan;
    }

    public void setRatePlan(RatePlans ratePlan) {
        this.ratePlan = ratePlan;
    }

    public int getHotelsId() {
        return HotelsId;
    }

    public void setHotelsId(int hotelsId) {
        HotelsId = hotelsId;
    }
}
