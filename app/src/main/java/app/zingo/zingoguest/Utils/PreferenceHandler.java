package app.zingo.zingoguest.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZingoHotels Tech on 18-09-2018.
 */

public class PreferenceHandler  {

    private static PreferenceHandler preferenceHandler = null;
    private SharedPreferences sh = null;

    private PreferenceHandler(Context context)
    {
        sh = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static PreferenceHandler getInstance(Context context)
    {
        if(preferenceHandler == null) {
            preferenceHandler = new PreferenceHandler(context);
        }
        return preferenceHandler;

    }

    public void clear(){
        sh.edit().clear().apply();

    }

    public void setUserId(int id)
    {
        sh.edit().putInt(Constants.USER_ID,id).apply();
    }

    public int getUserId()
    {
        return sh.getInt(Constants.USER_ID,0);
    }

    public void setUserName(String hotelName) {
        sh.edit().putString(Constants.USER_NAME,hotelName).apply();
    }

    public String getUserName()
    {
        return sh.getString(Constants.USER_NAME,"");
    }

    public void setPhoneNumber(String mobilenumber)
    {
        sh.edit().putString(Constants.USER_PHONENUMBER,mobilenumber).apply();
    }

    public String getPhoneNumber()
    {
        return sh.getString(Constants.USER_PHONENUMBER,"");
    }

    public void setHotelId(int id)
    {
        sh.edit().putInt(Constants.HOTEL_ID,id).apply();
    }

    public int getHotelId()
    {
        return sh.getInt(Constants.HOTEL_ID,0);
    }

    public void setHotelName(String hotelName) {
        sh.edit().putString(Constants.HOTEL_NAME,hotelName).apply();
    }

    public String getHotelName()
    {
        return sh.getString(Constants.HOTEL_NAME,"");
    }

    public void setHotelPlace(String hotelPlace) {
        sh.edit().putString(Constants.HOTEL_PLACE,hotelPlace).apply();
    }

    public String getHotelPlace()
    {
        return sh.getString(Constants.HOTEL_PLACE,"");
    }

    public void setUserFullName(String fullname) {
        sh.edit().putString(Constants.USER_FULLNAME,fullname).apply();
    }

    public String getUserFullName()
    {
        return sh.getString(Constants.USER_FULLNAME,"");
    }

    public void setUserEmail(String email) {
        sh.edit().putString(Constants.USER_EMAIL,email).apply();
    }

    public String getUserEmail()
    {
        return sh.getString(Constants.USER_EMAIL,"");
    }
}
