package com.example.myandroidapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximity;
    private TextView statusTextView;
    private LinearLayout rootLayout;

    @Override
    protected void Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // יצירת ממשק דינמי
        rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(40, 40, 40, 40);
        rootLayout.setGravity(android.view.Gravity.CENTER);
        rootLayout.setBackgroundColor(0xFF121212); // רקע כהה בסיסי

        statusTextView = new TextView(this);
        statusTextView.setText("העבר יד מעל החיישן לשינוי צבעים");
        statusTextView.setTextColor(Color.WHITE);
        statusTextView.setTextSize(22);
        statusTextView.setGravity(android.view.Gravity.CENTER);
        rootLayout.addView(statusTextView);

        setContentView(rootLayout);

        // אתחול חיישן הקרבה
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && proximity != null) {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < event.sensor.getMaximumRange()) {
                // מצב קרוב: משנים את צבע הרקע ואת שורת הסטטוס לצבע מגניב (למשל סגול עמוק)
                updateSystemColors(0xFF6200EE, "מצב חיישן פעיל!");
            } else {
                // מצב רגיל: מחזירים לצבע כהה בסיסי
                updateSystemColors(0xFF121212, "העבר יד מעל החיישן לשינוי צבעים");
            }
        }
    }

    // פונקציה לשינוי צבע שורת הסטטוס של המכשיר בזמן אמת
    private void updateSystemColors(int color, String text) {
        rootLayout.setBackgroundColor(color);
        statusTextView.setText(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
