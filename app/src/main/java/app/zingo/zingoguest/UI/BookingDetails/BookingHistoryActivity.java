package app.zingo.zingoguest.UI.BookingDetails;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import app.zingo.zingoguest.Adapters.BookingHistoryViewPagerAdapter;
import app.zingo.zingoguest.R;

public class BookingHistoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_booking_history);

            //Toolbar toolbar = (Toolbar) findViewById(R.id.booking_history_toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            //setSupportActionBar(toolbar);


            setTitle("Bookings");

            tabLayout = (TabLayout) findViewById(R.id.booking_list_tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            viewPager = (ViewPager) findViewById(R.id.booking_list_view_pager);

            BookingHistoryViewPagerAdapter adapter = new BookingHistoryViewPagerAdapter(getSupportFragmentManager());

            //Adding adapter to pager
            viewPager.setAdapter(adapter);

            //Adding onTabSelectedListener to swipe views
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                BookingHistoryActivity.this.finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }




}
