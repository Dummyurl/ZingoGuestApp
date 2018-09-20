package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class HotelDocuments {

    @SerializedName("DocumentId")
    private int DocumentId;

    @SerializedName("DocumentType")
    private String DocumentType;

    @SerializedName("DocumentNumber")
    private String DocumentNumber;

    @SerializedName("Image")
    private String Image;

    @SerializedName("FrontSidePhoto")
    private String FrontSidePhoto;

    @SerializedName("BackSidePhoto")
    private String BackSidePhoto;

    @SerializedName("DocumentName")
    private String DocumentName;

    @SerializedName("Status")
    private String Status;

    @SerializedName("ReEnterDocumentNumber")
    private String ReEnterDocumentNumber;

    @SerializedName("HotelId")
    private int HotelId;

    public int getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(int documentId) {
        DocumentId = documentId;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFrontSidePhoto() {
        return FrontSidePhoto;
    }

    public void setFrontSidePhoto(String frontSidePhoto) {
        FrontSidePhoto = frontSidePhoto;
    }

    public String getBackSidePhoto() {
        return BackSidePhoto;
    }

    public void setBackSidePhoto(String backSidePhoto) {
        BackSidePhoto = backSidePhoto;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReEnterDocumentNumber() {
        return ReEnterDocumentNumber;
    }

    public void setReEnterDocumentNumber(String reEnterDocumentNumber) {
        ReEnterDocumentNumber = reEnterDocumentNumber;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }
}
