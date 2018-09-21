package app.zingo.zingoguest.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.zingoguest.UI.BookingDetails.Fragments.ActiveBookingFragment;
import app.zingo.zingoguest.UI.BookingDetails.Fragments.CancelBooking;
import app.zingo.zingoguest.UI.BookingDetails.Fragments.CompletedBooking;
import app.zingo.zingoguest.UI.BookingDetails.Fragments.UpcomingBooking;

/**
 * Created by ZingoHotels Tech on 21-09-2018.
 */

public class BookingHistoryViewPagerAdapter extends FragmentStatePagerAdapter {


    String[] tabTitles = {"ACTIVE","UPCOMING", "COMPLETED", "CANCEL"};

    public BookingHistoryViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {

        try{
            switch (position) {

                case 0:
                    ActiveBookingFragment activefragment = new ActiveBookingFragment();
                    return activefragment;

                case 1:
                    UpcomingBooking upcomingFragment = new UpcomingBooking();
                    return upcomingFragment;


                case 2:
                    CompletedBooking completedFragment = new CompletedBooking();
                    return completedFragment;

                case 3:
                    CancelBooking cancelledFragment = new CancelBooking();
                    return cancelledFragment;


                default:
                    return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
