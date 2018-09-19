package app.zingo.zingoguest.UI.LandingScreens;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import app.zingo.zingoguest.BuildConfig;
import app.zingo.zingoguest.R;
import app.zingo.zingoguest.Utils.PreferenceHandler;

public class SplashScreen extends AppCompatActivity {

    //Ui declaration
    private static TextView mVersionName,mAppName,mPowered,mCopyRights;
    private static ImageView appLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash_screen);

            //Ui initialization
            mVersionName = (TextView) findViewById(R.id.version_name);
            mAppName = (TextView) findViewById(R.id.app_name);
            mPowered = (TextView) findViewById(R.id.powered_by);
            mCopyRights = (TextView) findViewById(R.id.copyrights_by);
            appLogo = (ImageView) findViewById(R.id.guest_splash);

            init();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void init(){
        try{

            mVersionName.setText("Version code : "+ BuildConfig.VERSION_NAME+"");

            Animation pop_anim = AnimationUtils.loadAnimation(this,R.anim.pop_up);
            Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
            Animation right_anim = AnimationUtils.loadAnimation(this,R.anim.right_trans);
            Animation left_anim = AnimationUtils.loadAnimation(this,R.anim.left_trans);
            mAppName.startAnimation(pop_anim);
            appLogo.startAnimation(fade_in);
            mPowered.startAnimation(right_anim);
            mCopyRights.startAnimation(left_anim);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    String mobilenumber = PreferenceHandler.getInstance(SplashScreen.this).getPhoneNumber();


                    if(mobilenumber.equals(""))
                    {
                        Intent signinintent = new Intent(SplashScreen.this,GuestLoginScreen.class);
                        startActivity(signinintent);
                        finish();
                    }
                    else
                    {
                        Intent mainActivityIntent = new Intent(SplashScreen.this, WelcomeScreen.class);
                        startActivity(mainActivityIntent);
                        finish();
                    }

                }
            }, 5000);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
