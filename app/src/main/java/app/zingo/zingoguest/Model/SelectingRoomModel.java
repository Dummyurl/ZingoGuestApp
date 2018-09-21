package app.zingo.zingoguest.Model;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 21-09-2018.
 */

public class SelectingRoomModel implements Serializable {

    private Rooms rooms;
    private String selectingRoom;
    public boolean isSelected;

    public SelectingRoomModel(String selectingRoom, Rooms rooms)
    {
        this.selectingRoom = selectingRoom;
        this.rooms = rooms;
        //this.isSelected = isSelected;
    }

    public SelectingRoomModel() {}

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getIsSelected()
    {
        return isSelected;
    }
    public void setSelectingRoom(String selectingRoom) {
        selectingRoom = selectingRoom;
    }


    public String getSelectingRoom() {
        return selectingRoom;
    }
}
