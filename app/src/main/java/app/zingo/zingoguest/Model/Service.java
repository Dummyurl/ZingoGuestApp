package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class Service implements Serializable {

    @SerializedName("ServicesId")
    private int ServicesId;

    @SerializedName("Description")
    private String Description;

    @SerializedName("Amount")
    private int Amount;

    @SerializedName("BookingNumber")
    private String BookingNumber;

    @SerializedName("BookingId")
    private int BookingId;

    @SerializedName("PaidStatus")
    private String PaidStatus;

    @SerializedName("PaymentMode")
    private String PaymentMode;

    @SerializedName("PaymentDate")
    private String PaymentDate;

    //PaymentDate
    @SerializedName("Quantity")
    private int Quantity;

    @SerializedName("ServiceStatus")
    private String ServiceStatus;

    public int getServicesId() {
        return ServicesId;
    }

    public void setServicesId(int servicesId) {
        ServicesId = servicesId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getBookingNumber() {
        return BookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        BookingNumber = bookingNumber;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public String getPaidStatus() {
        return PaidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        PaidStatus = paidStatus;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getServiceStatus() {
        return ServiceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        ServiceStatus = serviceStatus;
    }
}
