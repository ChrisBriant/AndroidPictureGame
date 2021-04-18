package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.data.DatabaseHandler;
import chrisbriant.uk.picturegame.services.PictureEvents;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import chrisbriant.uk.picturegame.services.GameServerConnection;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client;
    GameServerConnection conn;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create database
        db = new DatabaseHandler(this);
        //db.purge();

        conn = new GameServerConnection(this, db);

        EditText enterNameTxt = findViewById(R.id.enterNameTxt);
        Button sendNameBtn = findViewById(R.id.sendNameBtn);

        Context ctx = this.getApplicationContext();
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        sendNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("type", "name");
                    payload.put("client_id", sharedPrefs.getString("id",""));
                    payload.put("name",enterNameTxt.getText());
                    conn.send(payload.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AUTH", "Exception Fired");
                }
            }
        });

        SharedPreferences.OnSharedPreferenceChangeListener sharedPrefListener;
        sharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("Shared pref listener", "Shared prefs changed");
            }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefListener);

//        GameServerConnection.PictureEventListener listener = new GameServerConnection.PictureEventListener() {
//            @Override
//            public void onMessage(String data) {
//                Log.d("LISTENER",data);
//            }
//        };

    }

//    private void getWebservice(Context ctx) {
//        SSLContext sslContext = null;
//        try {
//            sslContext = SSLContext.getInstance("TLS");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        if(sslContext != null) {
//            // Create a trust manager that does not validate certificate chains
//            final X509TrustManager trustManager = new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//            };
//            try {
//                sslContext.init(null, new TrustManager[] { trustManager }, null);
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            }
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            Log.d("HELLO", "Hello");
//            final Request request = new Request.Builder().url("wss://picgameserver.chrisbriant.uk:8080/ws")
//                    .build();
//
//            client = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslSocketFactory, trustManager)
//                    .build();
//
//            WebSocketListener webSocketListener = new WebSocketListener() {
//                @Override
//                public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
//                    //super.onOpen(webSocket, response);
//                    Log.d("Connecting", "Hello");
//                }
//
//                @Override
//                public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
//                    super.onClosing(webSocket, code, reason);
//                }
//
//                @Override
//                public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
//                    //super.onFailure(webSocket, t, response);
//                    Log.d("Failure", t.getMessage());
//                }
//
//                @Override
//                public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
//                    super.onMessage(webSocket, text);
//                }
//
//                @Override
//                public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
//                    super.onMessage(webSocket, bytes);
//                }
//            };
//
//            client.newWebSocket(request, webSocketListener);
//            client.dispatcher().executorService().shutdown();
//        }


//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("OK HTTP","Failure !");
//                    }
//                });
//            }
//            @Override
//            public void onResponse(Call call, final Response response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Log.d("OK HTTP","Hello");
//                        } catch (Exception e) {
//                            Log.d("OK HTTP","Error during get body");
//                        }
//                    }
//                });
//            }
//        });
//    }
}
