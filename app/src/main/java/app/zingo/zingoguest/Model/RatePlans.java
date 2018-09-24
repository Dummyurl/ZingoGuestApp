package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class RatePlans {

    @SerializedName("RatePlanId")
    private int RatePlanId;

    @SerializedName("RatePlanName")
    private String RatePlanName;

    @SerializedName("MealPlan")
    private String MealPlan;

    @SerializedName("ActivationFlag")
    private boolean ActivationFlag;

    @SerializedName("PaymentMode")
    private String PaymentMode;

    @SerializedName("RoomCategoryId")
    private int RoomCategoryId;

    @SerializedName("RoomCategory")
    private RoomCategories RoomCategory;

    public int getRatePlanId() {
        return RatePlanId;
    }

    public void setRatePlanId(int ratePlanId) {
        RatePlanId = ratePlanId;
    }

    public String getRatePlanName() {
        return RatePlanName;
    }

    public void setRatePlanName(String ratePlanName) {
        RatePlanName = ratePlanName;
    }

    public String getMealPlan() {
        return MealPlan;
    }

    public void setMealPlan(String mealPlan) {
        MealPlan = mealPlan;
    }

    public boolean isActivationFlag() {
        return ActivationFlag;
    }

    public void setActivationFlag(boolean activationFlag) {
        ActivationFlag = activationFlag;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public int getRoomCategoryId() {
        return RoomCategoryId;
    }

    public void setRoomCategoryId(int roomCategoryId) {
        RoomCategoryId = roomCategoryId;
    }

    public RoomCategories getRoomCategory() {
        return RoomCategory;
    }

    public void setRoomCategory(RoomCategories roomCategory) {
        RoomCategory = roomCategory;
    }
}
