package app.zingo.zingoguest.UI.LandingScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.zingo.zingoguest.CustomViews.CustomFontTextView;
import app.zingo.zingoguest.Model.Traveller;
import app.zingo.zingoguest.Model.TravellerDeviceMapping;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.Constants;
import app.zingo.zingoguest.Utils.PreferenceHandler;
import app.zingo.zingoguest.Utils.SharedPrefManager;
import app.zingo.zingoguest.Utils.ThreadExecuter;
import app.zingo.zingoguest.Utils.Util;
import app.zingo.zingoguest.WebAPI.TravellerApi;
import app.zingo.zingoguest.WebAPI.TravellerMapApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestLoginScreen extends AppCompatActivity {

    private static LinearLayout mNumberLay,mOtpLay;
    private static CountryCodePicker mCountry;
    private static TextInputEditText mPhone,mOtp;
    private static AppCompatButton mVerifyNum,mVerifyCode;
    private static CustomFontTextView mMobileNumWithCountry;
    private static AppCompatTextView mCancel,mResend,mTimer;

    //Traveller
    Traveller dto;

    //Firebase & Auth
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private FirebaseAuth mAuth;
    private static final String TAG = "GuestLoginScreen";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_guest_login_screen);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            mNumberLay = (LinearLayout)findViewById(R.id.number_layout);
            mOtpLay = (LinearLayout)findViewById(R.id.otp_layout);

            mCountry = (CountryCodePicker)findViewById(R.id.ccp);

            mPhone = (TextInputEditText)findViewById(R.id.phone);
            mOtp = (TextInputEditText)findViewById(R.id.otp);

            mVerifyNum = (AppCompatButton)findViewById(R.id.verify_number);
            mVerifyCode = (AppCompatButton)findViewById(R.id.verify_code);

            mMobileNumWithCountry = (CustomFontTextView)findViewById(R.id.mobile_number_text);

            mCancel = (AppCompatTextView)findViewById(R.id.cancel);
            mResend = (AppCompatTextView)findViewById(R.id.resend);
            mTimer = (AppCompatTextView)findViewById(R.id.timer);

            mVerifyNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String phoneNumber = mPhone.getText().toString();
                    if(phoneNumber==null||phoneNumber.isEmpty()){
                        mPhone.setError("Please enter mobile number");
                        mPhone.requestFocus();
                    }else{

                        getTravelerByPhone(phoneNumber);

                    }
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dto = new Traveller();
                    mOtpLay.setVisibility(View.GONE);
                    mNumberLay.setVisibility(View.VISIBLE);
                    mPhone.setText("");
                }
            });

            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {

                    Log.d(TAG, "onVerificationCompleted:" + credential);

                    mVerificationInProgress = false;

                    String code = credential.getSmsCode();
                    mOtp.setText(code);

                    System.out.println("Code = "+code);

                    signInWithPhoneAuthCredential(credential);


                }

                @Override
                public void onVerificationFailed(FirebaseException e) {

                    mVerificationInProgress = false;

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        mPhone.setError("Invalid phone number.");
                    } else if (e instanceof FirebaseTooManyRequestsException) {

                        Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                    Log.d(TAG, "onCodeSent:" + verificationId);


                    mVerificationId = verificationId;
                    mResendToken = token;
                    Toast.makeText(GuestLoginScreen.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

                }
            };

            mVerifyCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOtp.getText().toString()==null||mOtp.getText().toString().isEmpty()){
                        mOtp.setError("Please enter otp");
                    }else{
                        verifyPhoneNumberWithCode(mVerificationId,mOtp.getText().toString());
                    }

                }
            });

            mResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resendVerificationCode(mCountry.getSelectedCountryCodeWithPlus()+mPhone.getText().toString(),mResendToken);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getTravelerByPhone(final String phoneNumber){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                TravellerApi apiService = Util.getClient().create(TravellerApi.class);
                Call<ArrayList<Traveller>> call = apiService.fetchTravelerByPhone(Constants.auth_string,phoneNumber);

                call.enqueue(new Callback<ArrayList<Traveller>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Traveller>> call, Response<ArrayList<Traveller>> response) {

                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            ArrayList<Traveller> list = response.body();

                            if (list.size()!=0) {
                                dto = list.get((list.size()-1));

                                if (dto != null) {

                                    if(dto.getTravellerId()!=0){

                                        mNumberLay.setVisibility(View.GONE);
                                        mOtpLay.setVisibility(View.VISIBLE);
                                        mMobileNumWithCountry.setText(""+mCountry.getSelectedCountryCodeWithPlus()+mPhone.getText().toString());
                                        startPhoneNumberVerification(mCountry.getSelectedCountryCodeWithPlus()+mPhone.getText().toString());



                                    }

                                }
                            }


                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(GuestLoginScreen.this, "Failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Traveller>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }


    //Firebase function
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

        mVerificationInProgress = true;

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() ) {

                            String token = SharedPrefManager.getInstance(GuestLoginScreen.this).getDeviceToken();

                            TravellerDeviceMapping hm = new TravellerDeviceMapping();
                            hm.setTravellerId(dto.getTravellerId());
                            hm.setDeviceId(token);
                            addDeviceId(hm);
                            System.out.println("Token = "+token);



                        } else {

                            Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                mOtp.setError("Invalid code.");

                            }

                        }
                    }
                });
    }


    public void addDeviceId(final TravellerDeviceMapping hm)
    {
        final ProgressDialog dialog = new ProgressDialog(GuestLoginScreen.this);
        dialog.setMessage("Adding Device..");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                //String authenticationString = Util.getToken(HotelListActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerMapApi travellerMapApi = Util.getClient().create(TravellerMapApi.class);
                Call<TravellerDeviceMapping> response = travellerMapApi.registerdevice(hm);

                response.enqueue(new Callback<TravellerDeviceMapping>() {
                    @Override
                    public void onResponse(Call<TravellerDeviceMapping> call, Response<TravellerDeviceMapping> response) {
                        System.out.println("GetHotelByProfileId = "+response.code());

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200||response.code() == 201||response.code() == 202||response.code() == 204)
                        {
                            TravellerDeviceMapping travellerMapresponse = response.body();

                            if(travellerMapresponse != null)
                            {
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserId(dto.getTravellerId());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserName(dto.getFirstName());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserFullName(dto.getFirstName());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserEmail(dto.getEmail());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setPhoneNumber(dto.getPhoneNumber());
                                Intent main = new Intent(GuestLoginScreen.this, WelcomeScreen.class);
                                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                main.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(main);
                                GuestLoginScreen.this.finish();
                            }



                        }else if(response.code() == 404){
                            if(response.body()==null){
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserId(dto.getTravellerId());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserName(dto.getFirstName());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserFullName(dto.getFirstName());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setUserEmail(dto.getEmail());
                                PreferenceHandler.getInstance(GuestLoginScreen.this).setPhoneNumber(dto.getPhoneNumber());
                                Intent main = new Intent(GuestLoginScreen.this, WelcomeScreen.class);
                                main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                main.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(main);
                                GuestLoginScreen.this.finish();
                            }
                        }
                        else
                        {

                            Toast.makeText(GuestLoginScreen.this,"Check your internet connection or please try after some time",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<TravellerDeviceMapping> call, Throwable t) {
                        System.out.println("Failed");
                        System.out.println(" Exception = "+t.getMessage());
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(GuestLoginScreen.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

}
