package app.zingo.zingoguest.WebAPI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {

    @Multipart
    @POST("Upload/user/PostUserImage")
    Call<String> uploadProfileImage(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name);
}
