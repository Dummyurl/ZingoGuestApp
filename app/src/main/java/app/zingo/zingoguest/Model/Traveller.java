package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class Traveller implements Serializable {

    @SerializedName("BookingTravellerId")
    private int TravellerId;

    @SerializedName("FirstName")
    private String FirstName;

    @SerializedName("MiddleName")
    private String MiddleName;

    @SerializedName("LastName")
    private String LastName;

    @SerializedName("Company")
    private String Company;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("DOB")
    private String dob;

    @SerializedName("Email")
    private String Email;

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    @SerializedName("Address")
    private String Address;

    @SerializedName("PinCode")
    private String PinCode;

    @SerializedName("UserRoleId")
    private int UserRoleId;

    @SerializedName("PlaceName")
    private String PlaceName;

    @SerializedName("CustomerGST")
    private String CustomerGST;

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("bookingList")
    private ArrayList<Bookings> bookingList;

    @SerializedName("Images")
    private String Images;

    @SerializedName("Image")
    private byte[] Image;

    @SerializedName("Nationality")
    private String Nationality;

    @SerializedName("travellerDocumentsList")
    private ArrayList<TravellerDocuments> travellerDocumentsList;

    public int getTravellerId() {
        return TravellerId;
    }

    public void setTravellerId(int travellerId) {
        TravellerId = travellerId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        UserRoleId = userRoleId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public ArrayList<Bookings> getBooking() {
        return bookingList;
    }

    public void setBooking(ArrayList<Bookings> booking) {
        this.bookingList = booking;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public ArrayList<TravellerDocuments> getTravellerDocumentsList() {
        return travellerDocumentsList;
    }

    public void setTravellerDocumentsList(ArrayList<TravellerDocuments> travellerDocumentsList) {
        this.travellerDocumentsList = travellerDocumentsList;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getCustomerGST() {
        return CustomerGST;
    }

    public void setCustomerGST(String customerGST) {
        CustomerGST = customerGST;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public ArrayList<Bookings> getBookingList() {
        return bookingList;
    }

    public void setBookingList(ArrayList<Bookings> bookingList) {
        this.bookingList = bookingList;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
