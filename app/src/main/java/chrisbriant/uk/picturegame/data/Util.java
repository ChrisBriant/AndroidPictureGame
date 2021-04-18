package chrisbriant.uk.picturegame.data;

public class Util {

    //For database handling
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "rooms";

    //Room table
    public static final String ROOM_TABLE_NAME = "room_tbl";
    public static final String KEY_ROOM_ID = "_id";
    public static final String ROOM_NAME_COL = "roomname";
    public static final String ROOM_OWNER_COL = "owner";
    public static final String ROOM_STATUS_COL ="status";

    //player table
    public static final String PLAYER_TABLE_NAME = "player_tbl";
    public static final String KEY_PLAYER_ID = "_id";
    public static final String FK_ROOM_ID = "_room_id";
    public static final String PLAYER_NAME_COL = "name";

}
