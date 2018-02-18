package com.fluffypeople.pillclock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.fluffypeople.pillclock.PillclockApplication.CONFIG_LAST_PILL;
import static com.fluffypeople.pillclock.PillclockApplication.PREFERENCES;

/**
 *
 * Created by osric on 11/02/18.
 */

public class PillclockAppWidgetProvider extends AppWidgetProvider {

    private static final int startHandColor = 0xFF00FF00;
    private static final int endHandColor = 0xFFFFFFFF;
    private static final int sectorColor = 0x88888888;

    static Calendar getLastPill(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        long lastPill = prefs.getLong(CONFIG_LAST_PILL, -1);

        if (lastPill == -1) {
            Log.i("getLastPill", "Last Pill not known, setting to now");
            lastPill = setLastPill(context);
        }

        Calendar result = new GregorianCalendar();
        result.setTimeInMillis(lastPill);

        return result;
    }

    static long setLastPill(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);

        long lastPill = System.currentTimeMillis();

        prefs.edit()
                .putLong(CONFIG_LAST_PILL, lastPill)
                .apply();

        return lastPill;
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(PillclockApplication.ACTION_TICK)) {
            ComponentName cn = new ComponentName(context, this.getClass());
            updateWidget(context, cn);
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName cn = new ComponentName(context, this.getClass());
        updateWidget(context, cn);
    }

    private void updateWidget(Context context, ComponentName cn) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(cn);
        Bitmap clock = drawClock(context);

        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.pillclock_appwidget);
            view.setBitmap(R.id.imageView, "setImageBitmap", clock);
            view.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, view);
        }
    }

    private Bitmap drawClock(Context context) {
        int width, height;

        Calendar lastPill = getLastPill(context);

        Calendar now = new GregorianCalendar();

        Bitmap clock = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock_face);

        width = clock.getWidth();
        height = clock.getHeight();

        Bitmap hand = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_short);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(sectorColor);

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

        p.setColorFilter(new PorterDuffColorFilter(endHandColor, PorterDuff.Mode.SRC_IN));
        canvas.save();
        canvas.rotate(nowAngle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(hand, 0, 0, p);
        canvas.restore();

        p.setColorFilter(new PorterDuffColorFilter(startHandColor, PorterDuff.Mode.SRC_IN));
        canvas.save();
        canvas.rotate(lastPillAngle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(hand, 0, 0, p);
        canvas.restore();

        return result;
    }

}



