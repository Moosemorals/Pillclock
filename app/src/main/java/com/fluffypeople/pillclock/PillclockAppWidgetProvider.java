package com.fluffypeople.pillclock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
 * Manage the widget
 * Created by osric on 11/02/18.
 */

public class PillclockAppWidgetProvider extends AppWidgetProvider {

    // Some colors. Remember: ARGB, and FF is opaque.
    private static final int START_HAND_COLOR = 0xFF00FF00;
    private static final int END_HAND_COLOR = 0xFFFFFFFF;
    private static final int SECTOR_BASE_COLOR = 0xA0000000;
    private static final int SECTOR_ALERT_COLOR = 0x40880000;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            action = "";
        }

        Log.d("onReceive", "Action: " + action);

        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
            case PillclockApplication.ACTION_ENABLE_ALARM:
                PillclockApplication.enableAlarm(context);
                break;
            case PillclockApplication.ACTION_TICK:
                updateWidget(context);

            default:
                super.onReceive(context, intent);
                break;
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateWidget(context);
    }

    private void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        Bitmap clock = drawWidget(context);

        for (int appWidgetId : appWidgetIds) {
            Log.d("updateWidget", "Updating widget " + appWidgetId);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.pillclock_appwidget);
            view.setBitmap(R.id.imageView, "setImageBitmap", clock);
            view.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, view);
        }
    }

    /**
     * Calculate the angle of time.
     *
     * @param when
     * @return
     */
    private float calculateAngle(Calendar when) {
        // Convert the time into a number of minutes from 12 noon or 12 midnight
        float angle = (when.get(Calendar.HOUR_OF_DAY) % 12f) * 60f + when.get(Calendar.MINUTE);
        // Scale it to between 0 and 1;
        angle = angle / (12f * 60f);
        // Multiply up by 360 to get an angle
        angle *= 360f;

        return angle;
    }

    /**
     * Draw the bitmap that the widget displays. A clock face, with an arc segment
     * showing how far its been between when the pill was taken, and now.
     *
     * @param context A valid context
     * @return The newly constructed Bitmap
     */
    private Bitmap drawWidget(Context context) {
        int width, height;

        // Get the time of the last pill, and make a note of the current time
        Calendar lastPill = PillclockApplication.getLastPill(context);
        Calendar now = new GregorianCalendar(PillclockApplication.LOCALE);

        long pillAge = (now.getTimeInMillis() - lastPill.getTimeInMillis()) / (60 * 60 * 1000);

        // Get angles from times
        float lastPillAngle = calculateAngle(lastPill);
        float nowAngle = calculateAngle(now);

        // Android measures angles clockwise from 3 O'Clock, so calculate the start angle
        // as lastPillAngle - 90 degrees
        float startAngle = lastPillAngle - 90;
        // Sweep is how far round the arc we need to go. Now minus lastPillAngle
        float sweepAngle = (nowAngle - lastPillAngle);
        // Modulus is irritating. If sweep is negative, make sure it's positive.
        while (sweepAngle < 0) {
            sweepAngle += 360;
        }
        // and *then* do the modulus.
        sweepAngle = (sweepAngle % 360);

        // Pull the clock from resources
        Bitmap clock = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock_face);
        // Everything is sized relative to the clock face.
        width = clock.getWidth();
        height = clock.getHeight();

        // Create a new Bitmap the same size as the clock face
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap result = Bitmap.createBitmap(width, height, conf);
        Canvas canvas = new Canvas(result);
        // And draw the clock into it.
        // TODO: Maybe we can do this as one step. Not sure we need to.
        canvas.drawBitmap(clock, 0, 0, null);
        clock.recycle();

        // Create a paint, and use it to draw the arc sector
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(SECTOR_BASE_COLOR);

        // If it's been more than 12 hours, draw an arc over the top in red

        float factor = 0.05f;
        RectF bounds = new RectF(factor * width, factor * height, (1 - factor) * width, (1 - factor) * height);
        if (pillAge > 12) {
            canvas.drawArc(bounds, 0, 360, true, p);
            p.setColor(SECTOR_ALERT_COLOR);
            canvas.drawArc(bounds, startAngle, sweepAngle, true, p);
        } else {
            canvas.drawArc(bounds, startAngle, sweepAngle, true, p);
        }


        // Pull the hand from resources
        Bitmap hand = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand_short);

        // I'm not sure exactly how this works, but setup the paint to change the color of the hand
        // Then rotate the bitmap to draw the start hand
        p.setColorFilter(new PorterDuffColorFilter(END_HAND_COLOR, PorterDuff.Mode.SRC_IN));
        canvas.save();
        canvas.rotate(nowAngle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(hand, 0, 0, p);
        canvas.restore();

        // And again for the end hand
        p.setColorFilter(new PorterDuffColorFilter(START_HAND_COLOR, PorterDuff.Mode.SRC_IN));
        canvas.save();
        canvas.rotate(lastPillAngle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(hand, 0, 0, p);
        canvas.restore();

        // And done.
        return result;
    }

}



