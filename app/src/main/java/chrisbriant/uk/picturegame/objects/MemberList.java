package chrisbriant.uk.picturegame.objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MemberList {
    private ArrayList<String> members;

    public MemberList() {
        members = new ArrayList<String>();
    }

    //Takes the json string, converts to
    public ArrayList<String> loadMembers(String roomList, String room) throws JSONException {
        //Clear the current hashtable

        ArrayList<String> listOfMembers = new ArrayList<String>();

        JSONArray roomsJSON = new JSONArray(roomList);

        for(int i=0;i<roomsJSON.length(); i++) {
            JSONObject roomJSON = new JSONObject(roomsJSON.get(i).toString());
            String roomName = roomJSON.getString("name");
            JSONArray members = roomJSON.getJSONArray("user_names");
            ArrayList<String> userNames = new ArrayList<String>();
            Log.d("PLAYERS",members.toString());
            Log.d("PLAYERS",roomName);
            Log.d("PLAYERS",room);
            if(roomName.equals(room)) {
                Log.d("PLAYERS","Room is equal");
                for (int j = 0; j < members.length(); j++) {
                    this.members.add(members.getString(j));
                }
            }
        }
        return this.members;
    }

    public ArrayList<String> getMembers() {
        return members;
    }
}
