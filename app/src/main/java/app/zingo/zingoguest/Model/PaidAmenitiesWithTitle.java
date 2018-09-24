package app.zingo.zingoguest.Model;

import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class PaidAmenitiesWithTitle {

    private String title;
    private ArrayList<PaidAmenities> list;

    public PaidAmenitiesWithTitle()
    {

    }

    public PaidAmenitiesWithTitle(String title,ArrayList<PaidAmenities> list)
    {
        this.list = list;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<PaidAmenities> getList() {
        return list;
    }

    public void setList(ArrayList<PaidAmenities> list) {
        this.list = list;
    }
}
