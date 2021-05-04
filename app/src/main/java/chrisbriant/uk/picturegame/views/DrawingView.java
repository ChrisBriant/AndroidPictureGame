package chrisbriant.uk.picturegame.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chrisbriant.uk.picturegame.objects.PicPoint;
import chrisbriant.uk.picturegame.objects.PicturePayload;
import chrisbriant.uk.picturegame.services.GameServerConn;

public class DrawingView extends View {
    private Paint mPaint;
    private Path mPath;
    private ArrayList<PicPoint> points = new ArrayList<PicPoint>();
    private boolean locked;
    private GameServerConn conn;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //points = new ArrayList<PicPoint>();
        if(!locked) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    mPath.moveTo(event.getX(), event.getY());
                    points.add(new PicPoint(Math.round(event.getX()), Math.round(event.getY()), "s"));
                    Log.d("DOWN", String.valueOf(event.getX()) + "," + String.valueOf(event.getY()));
                    break;

                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(event.getX(), event.getY());
                    points.add(new PicPoint(Math.round(event.getX()), Math.round(event.getY()), ""));
                    Log.d("MOVE", String.valueOf(event.getX()) + "," + String.valueOf(event.getY()));
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    points.add(new PicPoint(Math.round(event.getX()), Math.round(event.getY()), "e"));
                    //Construct payload
                    conn = GameServerConn.getInstance(this.getContext());
                    Log.d("UP", "I am trying to connect");
                    SharedPreferences sharedPrefs = conn.getSharedPrefs();
                    PicturePayload payload = new PicturePayload(sharedPrefs.getString("id", ""), sharedPrefs.getString("gameId", ""), points);
                    JSONObject picturePayload = payload.getJSONPayload();
                    Log.d("UP", picturePayload.toString());
                    //Send the payload
                    conn.send(picturePayload.toString());
                    break;
            }
        }

        return true;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void drawPoints(String picData) {
        try {
            JSONArray picArray = new JSONArray(picData);
            for(int i=0;i<picArray.length();i++) {
                JSONObject pointJson = picArray.getJSONObject(i);
                PicPoint point = new PicPoint(pointJson.getInt("x"),
                        pointJson.getInt("y"),
                        pointJson.getString("pos")
                );
                //Draw to canvas
                switch(point.getPos()) {
                    //Multiplying by four to get the scale
                    case "s":
                        mPath.moveTo(point.getX()*4, point.getY()*4);
                    case "e":
                        mPath.lineTo(point.getX()*4, point.getY()*4);
                        mPath.close();
                    default:
                        mPath.lineTo(point.getX()*4, point.getY()*4);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //this.refreshDrawableState();
        this.invalidate();
    }
}
