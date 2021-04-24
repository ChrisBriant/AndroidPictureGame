package chrisbriant.uk.picturegame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

import chrisbriant.uk.picturegame.objects.PicPoint;
import chrisbriant.uk.picturegame.objects.PicturePayload;

public class DrawingView extends View {
    private Paint mPaint;
    private Path mPath;
    private ArrayList<PicPoint> points = new ArrayList<PicPoint>();

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

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                points.add(new PicPoint(Math.round(event.getX()),Math.round(event.getY()),"s"));
                Log.d("DOWN", String.valueOf(event.getX()) + "," + String.valueOf(event.getY()));
                break;

            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                points.add(new PicPoint(Math.round(event.getX()),Math.round(event.getY()),""));
                Log.d("MOVE", String.valueOf(event.getX()) + "," + String.valueOf(event.getY()));
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                points.add(new PicPoint(Math.round(event.getX()),Math.round(event.getY()),"e"));
                PicturePayload payload = new PicturePayload("2f954fd7-5fe5-40e3-9a26-36be87d0a52f","5504efaf-2bce-4d35-a3a6-fc13105e9e18",points);
                JSONObject picturePayload = payload.getJSONPayload();
                Log.d("UP",picturePayload.toString());
                break;
        }

        return true;
    }
}
