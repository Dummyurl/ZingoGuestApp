package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class FeedBack {

    @SerializedName("FeedBackId")
    private int feedbackId;

    @SerializedName("StarRating")
    private String starRating;

    @SerializedName("Comment")
    private String comment;

    @SerializedName("BookingId")
    private int bookingId;

    @SerializedName("FeedbackDate")
    private String FeedbackDate;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getFeedbackDate() {
        return FeedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        FeedbackDate = feedbackDate;
    }
}
