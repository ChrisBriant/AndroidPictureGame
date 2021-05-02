package chrisbriant.uk.picturegame.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.activities.GameActivity;
import chrisbriant.uk.picturegame.activities.RoomListActivity;
import chrisbriant.uk.picturegame.data.DatabaseHandler;
import chrisbriant.uk.picturegame.objects.PicPoint;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class GameServerConn {
    // static variable single_instance of type Singleton
    private static GameServerConn single_instance = null;

    private OkHttpClient client;
    private SharedPreferences sharedPrefs;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String URL = "wss://picgameserver.chrisbriant.uk:8080/ws";
    private WebSocket sock;
    private PictureEvents picEvents;
    private DatabaseHandler db;

    private GameServerConn(Context ctx) {

        //Create share preferences
        sharedPrefs = ctx.getSharedPreferences(ctx.getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        //Try the custom listener
        picEvents = new PictureEvents();



        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(sslContext != null) {
            // Create a trust manager that does not validate certificate chains
            final X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };
            try {
                sslContext.init(null, new TrustManager[] { trustManager }, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            Log.d("HELLO", "Hello");
            final Request request = new Request.Builder().url("wss://picgameserver.chrisbriant.uk:8080/ws")
                    .build();

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            WebSocketListener webSocketListener = new WebSocketListener() {
                @Override
                public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                    //super.onOpen(webSocket, response);
                    Log.d("Connecting",response.message());
                    sock = webSocket;
                }

                @Override
                public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                    super.onClosing(webSocket, code, reason);
                }

                @Override
                public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                    //super.onFailure(webSocket, t, response);
                    Log.d("Failure", "The websocket has failed");
                }

                @Override
                public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                    //super.onMessage(webSocket, text);
                    Log.d("Message", text);
                    JSONObject reader;
                    //JSONObject jsonObj;
                    try {
                        reader = new JSONObject(text);
                        //jsonObj = reader.getJSONObject("type");
                        String type = reader.getString("type");
                        Log.d("TYPE",type);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Intent intent;
                        switch(type) {
                            case "register":
                                editor.putString("id", reader.getString("yourid"));
                                editor.apply();
                            case "set_name":
                                editor.putString("name",reader.getString("message"));
                                editor.apply();
                                intent = new Intent(ctx, RoomListActivity.class);
                                ctx.startActivity(intent);
                            case "room_list":
                                Log.d("EVENT","ROOM LIST HAPPENED");
                                //Maybe try
                                //https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android
                                //db.purge();
                                String roomList = reader.getString("rooms");
                                //db.createRoom(roomList);
                                editor.putString("room_list",roomList);
                                editor.apply();
                            case "room_failure":
                                Toast.makeText(ctx,"Room already exists.",Toast.LENGTH_SHORT).show();
                            case "game_start":
                                Log.d("GAME START", "Game start received");
                                editor.putString("startPlayer", reader.getString("startplayer"));
                                editor.putString("gameId", reader.getString("game_id"));
                                intent = new Intent(ctx, GameActivity.class);
                                ctx.startActivity(intent);
                                editor.apply();
                            case "new_round":
                                editor.putString("startPlayer", reader.getString("startplayer"));
                                editor.putString("gameId", reader.getString("game_id"));
                                intent = new Intent(ctx, GameActivity.class);
                                ctx.startActivity(intent);
                                editor.apply();
                            case "word":
                                editor.putString("word", reader.getString("word"));
                                editor.apply();
                            case "picture":
                                editor.putString("picture", reader.getString("picture"));
                                editor.apply();
                            case "win_from_give_up":
                                JSONArray wins = new JSONArray(sharedPrefs.getString("wins","[]"));
                                JSONObject win = new JSONObject();
                                win.put("winnerId",reader.getString("client_id"));
                                win.put("winnerName", reader.getString("client_name"));
                                win.put("roundNo",wins.length()+1);
                                wins.put(win);
                                Log.d("WIN FROM GIVE UP", wins.toString());
                                editor.putString("wins",wins.toString());
                        }
                    } catch (Exception e) {

                    }



                }

            };

            client.newWebSocket(request, webSocketListener);
            client.dispatcher().executorService().shutdown();
        }
    }

    private static ArrayList<PicPoint> getPoints(String picData) {
        ArrayList<PicPoint> points = new ArrayList<PicPoint>();
        try {
            JSONArray picArray = new JSONArray(picData);
            for(int i=0;i<picArray.length();i++) {
                JSONObject pointJson = picArray.getJSONObject(i);
                PicPoint point = new PicPoint(pointJson.getInt("x"),
                                            pointJson.getInt("y"),
                                            pointJson.getString("pos")
                );
                points.add(point);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return points;
    }

    private String post(String json) throws IOException {
//        Request request = new Request.Builder()
//                .url(URL)
//                .post(body)
//                .build();
//        client.
//        Response response = client.newCall(request).execute();
//        return response.body().string();
        return null;
    }

    public void send(String json) {
        Log.d("Sending", json);
        sock.send(json);
    }

    public static GameServerConn getInstance(Context ctx)
    {
        if (single_instance == null)
            single_instance = new GameServerConn(ctx);

        return single_instance;
    }

    public SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }

    //    public PictureEventListener getPicListener() {
//        return picListener;
//    }
}
