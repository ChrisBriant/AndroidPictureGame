package chrisbriant.uk.picturegame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME,null, Util.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table
        String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s VARCHAR(255), %s VARCHAR(255), %s BOOLEAN DEFAULT FALSE);",
                Util.ROOM_TABLE_NAME,
                Util.KEY_ROOM_ID,
                Util.ROOM_NAME_COL,
                Util.ROOM_OWNER_COL,
                Util.ROOM_STATUS_COL);
        Log.d("DB CREATE",CREATE_TABLE);
        db.execSQL(CREATE_TABLE);

        String CREATE_PLAYERS_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s VARCHAR(255) PRIMARY KEY, " +
                        "%s VARCHAR(255), " +
                        "%s INTEGER, " +
                        "CONSTRAINT fk_room FOREIGN KEY (%s) REFERENCES %s (%s) ON DELETE CASCADE);",
                Util.PLAYER_TABLE_NAME,
                Util.KEY_PLAYER_ID,
                Util.PLAYER_NAME_COL,
                Util.FK_ROOM_ID,
                Util.FK_ROOM_ID,
                Util.ROOM_TABLE_NAME,
                Util.KEY_ROOM_ID
        );
        Log.d("QUERY",CREATE_PLAYERS_TABLE);
        db.execSQL(CREATE_PLAYERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int createRoom(String JSONStr) {
        try {
            JSONArray roomsJSON = new JSONArray(JSONStr);

            for(int i=0;i<roomsJSON.length(); i++) {
                JSONObject roomJSON = new JSONObject(roomsJSON.get(i).toString());
                String roomName = roomJSON.getString("name");
                String owner = roomJSON.getString("owner_name");
                JSONArray members = roomJSON.getJSONArray("user_names");
                boolean status = roomJSON.getBoolean("locked");

                Log.d("JSON Data", roomName);
                Log.d("JSON Data", owner);
                Log.d("JSON Data", String.valueOf(members.length()));
                Log.d("JSON Data", String.valueOf(status));

                if(members.length() > 0) {
                    //Populate players table
                    //TODO  process usernames here
                }


            }
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }


//        SQLiteDatabase dba = this.getWritableDatabase();
//
//        Log.d("DATABASE", "here");
//
//        //Datetime from now
//        String createdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
//
//        ContentValues routeValues = new ContentValues();
//        routeValues.put(Util.ROUTE_NAME_COL, routeName);
//        routeValues.put(Util.CREATED_COL, createdDate);
//
//        int insertedId = (int) dba.insert(Util.ROUTE_TABLE_NAME,null,routeValues);
//
//        Log.d("Added Route", String.valueOf(insertedId));
//        dba.close();

        return 1;
    }

    public void purge() {
        SQLiteDatabase dba = this.getWritableDatabase();

        dba.execSQL(String.format("DELETE FROM %s;",Util.ROOM_TABLE_NAME));
        dba.execSQL(String.format("DELETE FROM %s;",Util.PLAYER_TABLE_NAME));
    }
}
