package app.zingo.zingoguest.UI.LandingScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.zingo.zingoguest.R;

public class GuestLoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_guest_login_screen);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
