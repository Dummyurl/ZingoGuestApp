package app.zingo.zingoguest.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 20-09-2018.
 */

public class TravellerDocuments implements Serializable {

    @SerializedName("DocumentId")
    private int DocumentId;

    @SerializedName("DocumentType")
    private String DocumentType;

    @SerializedName("DocumentNumber")
    private String DocumentNumber;

    @SerializedName("DocumentName")
    private String DocumentName;

    @SerializedName("Status")
    private String Status;

    @SerializedName("BookingTravellerId")
    private int TravellerId;

    @SerializedName("Image")
    private String Image;

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

    public int getTravellerId() {
        return TravellerId;
    }

    public void setTravellerId(int travellerId) {
        TravellerId = travellerId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
