package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class Payment implements Serializable {

    @SerializedName("PaymentId")
    private int PaymentId;

    @SerializedName("PaymentName")
    private String PaymentName;

    @SerializedName("Amount")
    private int Amount ;

    @SerializedName("PaymentType")
    private String PaymentType;

    @SerializedName("BookingId")
    private int BookingId;

    @SerializedName("PaymentDate")
    private String PaymentDate	;

    public int getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(int paymentId) {
        PaymentId = paymentId;
    }

    public String getPaymentName() {
        return PaymentName;
    }

    public void setPaymentName(String paymentName) {
        PaymentName = paymentName;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }
}
