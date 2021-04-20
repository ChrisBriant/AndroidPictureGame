package chrisbriant.uk.picturegame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chrisbriant.uk.picturegame.objects.RoomItem;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME,null, Util.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ON CREATE", "On Create Called");
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
                        "%s INTEGER PRIMARY KEY, " +
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

    public void createRoom(String JSONStr) {
        SQLiteDatabase dba = this.getWritableDatabase();
        int insertedId = 0;

        try {
            JSONArray roomsJSON = new JSONArray(JSONStr);

            Log.d("Rooms Data",String.valueOf(roomsJSON.length()));

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

                ContentValues roomValues = new ContentValues();
                roomValues.put(Util.ROOM_NAME_COL, roomName);
                roomValues.put(Util.ROOM_OWNER_COL, owner);
                if(status) {
                    roomValues.put(Util.ROOM_STATUS_COL,1);
                } else {
                    roomValues.put(Util.ROOM_STATUS_COL,0);
                }
                insertedId = (int) dba.insert(Util.ROOM_TABLE_NAME,null,roomValues);

                if(members.length() > 0) {
                    //Populate players table
                    //TODO  process usernames here
                    for(int j=0;j<members.length();j++) {
                        JSONObject memberObject = new JSONObject(members.get(j).toString());
                        Log.d("Member Json", memberObject.toString());
                    }
                }


            }
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }

        Log.d("INSERTED", String.valueOf(insertedId));
        dba.close();
        //return insertedId;
    }

    public void purge() {
        SQLiteDatabase dba = this.getWritableDatabase();

        //dba.execSQL(String.format("DROP DATABASE %s;",Util.DB_NAME));
        dba.execSQL(String.format("DELETE FROM %s;",Util.ROOM_TABLE_NAME));
        dba.execSQL(String.format("DELETE FROM %s;",Util.PLAYER_TABLE_NAME));
        //dba.execSQL(String.format("DROP TABLE %s",Util.ROOM_TABLE_NAME));
        //dba.execSQL(String.format("DROP TABLE %s",Util.PLAYER_TABLE_NAME));
        dba.close();

    }

    public ArrayList<RoomItem> getRooms() {
        SQLiteDatabase dba = this.getReadableDatabase();

        ArrayList<RoomItem> rooms = new ArrayList<RoomItem>();

        String Q_ROOMS_WITH_PLAYERS =
                String.format("SELECT r.%s,r.%s,r.%s,r.%s,pl.%s as player_id,pl.%s FROM %s AS r" +
                                " LEFT JOIN %s AS pl ON r.%s = pl.%s;",
                        Util.KEY_ROOM_ID,
                        Util.ROOM_NAME_COL,
                        Util.ROOM_OWNER_COL,
                        Util.ROOM_STATUS_COL,
                        Util.KEY_PLAYER_ID,
                        Util.PLAYER_NAME_COL,
                        Util.ROOM_TABLE_NAME,
                        Util.PLAYER_TABLE_NAME,
                        Util.KEY_ROOM_ID,
                        Util.FK_ROOM_ID
                );
        Log.d("EXECUTING QUERY", Q_ROOMS_WITH_PLAYERS);

        Cursor cursor = dba.rawQuery(Q_ROOMS_WITH_PLAYERS,null);

        Log.d("ROOMS FROM DB", String.valueOf(cursor.getCount()));

        //Tracking cursor
        int currRoomId = 0;
        if(cursor.moveToFirst()) {
            currRoomId = cursor.getInt(1);
            Log.d("CURRENT ROOM", String.valueOf(cursor.getCount()) );
            Log.d("CURRENT ROOM", cursor.getColumnNames().toString() );
            String[] cols = cursor.getColumnNames();
            for(int i=0;i<cols.length;i++) {
                Log.d("CURRENT ROOM", cols[i] );
            }
            Log.d("CURRENT ROOM", String.valueOf(cursor.getColumnCount()) );
        }

        if(cursor.moveToFirst()) {
            ArrayList<String> players = new ArrayList<String>();

           do {
               int newRoomId = cursor.getInt(cursor.getColumnIndex(Util.KEY_ROOM_ID));
               String roomName = cursor.getString(cursor.getColumnIndex(Util.ROOM_NAME_COL));
               String roomOwner = cursor.getString(cursor.getColumnIndex(Util.ROOM_OWNER_COL));
               boolean roomStatus = cursor.getInt(cursor.getColumnIndex(Util.ROOM_STATUS_COL)) > 0;
               //Add to room list if new room
               Log.d("ADDING ROOM",String.valueOf(currRoomId) + " ," + String.valueOf(newRoomId));
               if(currRoomId != newRoomId) {
                   //Add room object

                   rooms.add(new RoomItem(
                           newRoomId,
                           roomName,
                           roomOwner,
                           roomStatus,
                           players.size(),
                           players
                   ));
                   //Reset
                   currRoomId = newRoomId;
                   players = new ArrayList<String>();
               } else {
                   try {
                       players.add(cursor.getString(cursor.getColumnIndex(Util.PLAYER_NAME_COL)));
                   } catch(Exception e) {
                       Log.d("ERROR ADDING PLAYER", "Unable to add the player");
                   }
               }
           } while(cursor.moveToNext());

        }
        //dba.close();
        return rooms;
    }


    public RoomItem getRoom(int roomId) {
        SQLiteDatabase dba = this.getReadableDatabase();

        ArrayList<RoomItem> rooms = new ArrayList<RoomItem>();

        String Q_ROOMS_WITH_PLAYERS =
                String.format("SELECT r.%s,r.%s,r.%s,r.%s,pl.%s,pl.%s FROM %s AS r" +
                                " INNER JOIN %s AS pl ON r.%s = p.%s" +
                                " WHERE r.%s = %s;",
                        Util.KEY_ROOM_ID,
                        Util.ROOM_NAME_COL,
                        Util.ROOM_OWNER_COL,
                        Util.ROOM_STATUS_COL,
                        Util.KEY_PLAYER_ID,
                        Util.KEY_PLAYER_ID,
                        Util.ROOM_TABLE_NAME,
                        Util.PLAYER_TABLE_NAME,
                        Util.KEY_ROOM_ID,
                        Util.FK_ROOM_ID,
                        Util.KEY_ROOM_ID,
                        roomId
                );
        Log.d("EXECUTING QUERY", Q_ROOMS_WITH_PLAYERS);

        Cursor cursor = dba.rawQuery(Q_ROOMS_WITH_PLAYERS,null);

        //Tracking cursor
        String roomName = "";
        String roomOwner = "";
        boolean roomStatus = false;
        ArrayList<String> players = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            roomId = cursor.getInt(cursor.getColumnIndex(Util.KEY_ROOM_ID));
            roomName = cursor.getString(cursor.getColumnIndex(Util.ROOM_NAME_COL));
            roomOwner = cursor.getString(cursor.getColumnIndex(Util.ROOM_NAME_COL));
            roomStatus = cursor.getInt(cursor.getColumnIndex(Util.ROOM_STATUS_COL)) > 0;
        }

        if(cursor.moveToFirst()) {
            do {
                players.add(cursor.getString(cursor.getColumnIndex(Util.PLAYER_NAME_COL)));
            } while(cursor.moveToNext());
        }

        RoomItem room = new RoomItem(roomId,roomName,roomOwner,roomStatus,players.size(),players);

        //dba.close();
        return room;
    }
}
