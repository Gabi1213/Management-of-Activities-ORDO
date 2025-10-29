package com.example.ordo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DayRoutineActivity extends AppCompatActivity {

    private int startHour = 8, startMinute = 0;
    private int endHour = 9, endMinute = 0;

    private List<ActivityItem> activitiesList = new ArrayList<>();
    private LinearLayout activitiesContainer;
    private TextView noActivitiesPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_day_routine);

        TextView dateTextView = findViewById(R.id.text_date_tomorrow);
        Button addButton = findViewById(R.id.button_add_activity);

        activitiesContainer = findViewById(R.id.activities_list_container);
        noActivitiesPlaceholder = findViewById(R.id.text_no_activities_placeholder);

        // 1. LOGICA BUTONULUI DE MENIU POP-UP
        ImageButton popupMenuButton = findViewById(R.id.button_popup_menu_day);
        if (popupMenuButton != null) {
            popupMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });
        }

        // 2. Afișează data de mâine
        displayTomorrowDate(dateTextView);

        // 3. Setează acțiunea butonului "Adaugă Activitate"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddActivityDialog();
            }
        });

        // 4. Logică EdgeToEdge simplificată
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_day_routine_layout), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
                return insets;
            }
        });
    }

    private void displayTomorrowDate(TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ro", "RO"));
        String tomorrowDate = dateFormat.format(calendar.getTime());
        dateTextView.setText(tomorrowDate);
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_settings) {
                    Toast.makeText(DayRoutineActivity.this, "Setări selectate (Zi Rutină)", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_about) {
                    Toast.makeText(DayRoutineActivity.this, "Despre Aplicație selectat (Zi Rutină)", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_exit) {
                    finish();
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void displayActivity(ActivityItem item) {
        noActivitiesPlaceholder.setVisibility(View.GONE);

        TextView activityView = new TextView(this);
        String timeRange = "[" + item.getStartTime() + " - " + item.getEndTime() + "]";
        String displayString = timeRange + "  " + item.getName();

        activityView.setText(displayString);
        activityView.setTextSize(18);
        activityView.setPadding(0, 16, 0, 16);

        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        separator.setBackgroundColor(getColor(R.color.black));

        activitiesContainer.addView(activityView);
        activitiesContainer.addView(separator);
    }

    /**
     * Setează o notificare folosind AlarmManager pentru ziua de MÂINE.
     */
    private void scheduleNotification(ActivityItem item, int hour, int minute) {
        // 1. Calculează timpul alarmei (Mâine la ora specificată)
        Calendar alarmTime = Calendar.getInstance();

        // Adaugă o zi (pentru a seta alarma pentru mâine)
        alarmTime.add(Calendar.DAY_OF_MONTH, 1);

        // Setează ora și minutul din dialog
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);

        // 2. Creează Intent-ul (cu date suplimentare)
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("ACTIVITY_NAME", item.getName());
        intent.putExtra("START_TIME", item.getStartTime());

        // Folosim un RequestCode unic
        int requestCode = (int) System.currentTimeMillis();

        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? android.app.PendingIntent.FLAG_IMMUTABLE : 0)
        );

        // 3. Setează Alarma
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(this, "Notificare programată pentru MÂINE la " + item.getStartTime(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Metodă actualizată pentru a salva, afișa și programa notificarea.
     */
    private void showAddActivityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_activity, null);
        builder.setView(dialogView);

        final EditText activityNameEditText = dialogView.findViewById(R.id.edit_text_activity_name);
        final Button startTimeButton = dialogView.findViewById(R.id.button_start_time);
        final Button endTimeButton = dialogView.findViewById(R.id.button_end_time);

        startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
        endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute));

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(true, startTimeButton);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(false, endTimeButton);
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String name = activityNameEditText.getText().toString().trim();
                String startTimeStr = startTimeButton.getText().toString();
                String endTimeStr = endTimeButton.getText().toString();

                int startTimeInMinutes = startHour * 60 + startMinute;
                int endTimeInMinutes = endHour * 60 + endMinute;

                if (endTimeInMinutes <= startTimeInMinutes) {
                    Toast.makeText(this, "Eroare: Interval orar invalid! Ora de sfârșit trebuie să fie după ora de început.", Toast.LENGTH_LONG).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(this, "Eroare: Vă rugăm introduceți numele activității.", Toast.LENGTH_LONG).show();
                } else {
                    // Validare reușită

                    // 1. Creăm, Salvăm și Afișăm elementul
                    ActivityItem newItem = new ActivityItem(name, startTimeStr, endTimeStr);
                    activitiesList.add(newItem);
                    displayActivity(newItem);

                    // 2. PROGRAMEAZĂ NOTIFICAREA la ora de început (pentru mâine)
                    scheduleNotification(newItem, startHour, startMinute);

                    dialog.dismiss(); // Închide dialogul la succes
                }
            });
        });

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Adaugă", (dialogInterface, i) -> {});
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Anulează", (dialogInterface, i) -> dialogInterface.cancel());

        dialog.show();
    }

    private void showTimePicker(final boolean isStartTime, final Button targetButton) {
        int initialHour = isStartTime ? startHour : endHour;
        int initialMinute = isStartTime ? startMinute : endMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (isStartTime) {
                            startHour = hourOfDay;
                            startMinute = minute;
                        } else {
                            endHour = hourOfDay;
                            endMinute = minute;
                        }
                        targetButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }
                }, initialHour, initialMinute, true
        );
        timePickerDialog.show();
    }
}