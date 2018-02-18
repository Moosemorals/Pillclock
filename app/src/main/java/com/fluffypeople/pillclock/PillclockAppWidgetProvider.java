package com.fluffypeople.pillclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * Created by osric on 11/02/18.
 */

public class PillclockAppWidgetProvider extends AppWidgetProvider {



    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            Calendar lastPill = new GregorianCalendar(2017, 1, 18, 13, 25);

            Bitmap clock = drawClock(context, lastPill);

            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.pillclock_appwidget);
            view.setBitmap(R.id.imageView, "setImageBitmap", drawClock(context, lastPill));
            appWidgetManager.updateAppWidget(appWidgetId, view);
        }
    }


    private Bitmap drawClock(Context context, Calendar lastPill) {
        int width, height;

        Calendar now = new GregorianCalendar();

        Bitmap clock = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget0025);

        width = clock.getWidth();
        height = clock.getHeight();

        Bitmap hand = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget0024);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0x88888888);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap result = Bitmap.createBitmap(width, height, conf);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(clock, 0, 0, null);

        float lastPillAngle = (lastPill.get(Calendar.HOUR_OF_DAY) % 12f) * 60f + lastPill.get(Calendar.MINUTE);
        lastPillAngle = lastPillAngle / (12f * 60f);
        lastPillAngle *= 360f;

        float nowAngle = (now.get(Calendar.HOUR_OF_DAY) % 12f) * 60f + now.get(Calendar.MINUTE);
        nowAngle = nowAngle / (12f * 60f);
        nowAngle *= 360;

        float startAngle = lastPillAngle - 90;
        float sweepAngle = (nowAngle - lastPillAngle);
        while (sweepAngle < 0) {
            sweepAngle += 360;
        }

        sweepAngle = (sweepAngle % 360);

        canvas.drawArc(new RectF(0, 0, width, height), startAngle, sweepAngle, true, p);

        Log.d("drawClock", "Pill:" + lastPillAngle + ", now " + nowAngle);
        Log.d("drawClock", "From:" + startAngle + ", sweep " + sweepAngle);

        p.setColorFilter(new PorterDuffColorFilter(0xFF00FF00, PorterDuff.Mode.SRC_IN));
        canvas.save();
        canvas.rotate(lastPillAngle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(hand, 0, 0, p);
        canvas.restore();

        p.setColorFilter(new PorterDuffColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_IN));
        canvas.save();
        canvas.rotate(nowAngle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(hand, 0, 0, p);
        canvas.restore();

        return result;
    }



}
