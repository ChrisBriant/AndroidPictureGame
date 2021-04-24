package chrisbriant.uk.picturegame.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.activities.RoomListActivity;
import chrisbriant.uk.picturegame.data.DatabaseHandler;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class GameServerConnection {
    private OkHttpClient client;
    private SharedPreferences sharedPrefs;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String URL = "wss://picgameserver.chrisbriant.uk:8080/ws";
    private WebSocket sock;
    private PictureEvents picEvents;
    private DatabaseHandler db;

//    public interface PictureEventListener {
//        public void onMessage(String data);
//    }
//    private GameServerConnection.PictureEventListener picListener;
//
//    public void setCustomObjectListener(GameServerConnection.PictureEventListener listener) {
//        this.picListener = listener;
//    }

    public GameServerConnection(Context ctx, DatabaseHandler dbHandler) {
        db = dbHandler;

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
                    Log.d("Failure", t.getMessage());
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
                        switch(type) {
                            case "register":
                                editor.putString("id", reader.getString("yourid"));
                                editor.apply();
                                //picListener.onMessage("REGISTER");
                            case "set_name":
                                editor.putString("name",reader.getString("message"));
                                editor.apply();
                                Intent intent = new Intent(ctx, RoomListActivity.class);
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
                        }
                    } catch (Exception e) {

                    }



                }

            };

            client.newWebSocket(request, webSocketListener);
            client.dispatcher().executorService().shutdown();
        }
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

//    public PictureEventListener getPicListener() {
//        return picListener;
//    }
}
