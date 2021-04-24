package chrisbriant.uk.picturegame.objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PicturePayload {
    private String type = "picture";
    private String clientId;
    private String gameId;
    private ArrayList<PicPoint> picture;

    public PicturePayload(String clientId, String gameId, ArrayList<PicPoint> picture) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.picture = picture;
    }

    public String getClient_id() {
        return clientId;
    }

    public void setClient_id(String clientId) {
        this.clientId = clientId;
    }

    public String getGame_id() {
        return gameId;
    }

    public void setGame_id(String gameId) {
        this.gameId = gameId;
    }

    public ArrayList<PicPoint> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<PicPoint> picture) {
        this.picture = picture;
    }

    //Return a JSON representation of this object
    public JSONObject getJSONPayload() {
        JSONObject payload = new JSONObject();
        JSONArray points = new JSONArray();

        //Build JSON array of points
        for(PicPoint point : this.picture) {
            JSONObject pointJSON = new JSONObject();
            try {
                pointJSON.put("x", point.getX());
                pointJSON.put("y", point.getY());
                pointJSON.put("pos", point.getPos());
            } catch (JSONException e) {
                Log.d("JSON Error", e.getMessage());
            }
            points.put(pointJSON);
        }

        try {
            payload.put("type", "picture");
            payload.put("client_id", this.clientId);
            payload.put("game_id", this.gameId);
            payload.put("picture", points);
        } catch (JSONException e) {
            Log.d("JSON Error", e.getMessage());
        }

        return payload;
    }
}
