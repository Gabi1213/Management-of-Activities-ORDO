package com.example.ordo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // NOU: Import necesar
import android.widget.PopupMenu; // NOU: Import necesar
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RoutineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routine);

        // 1. LOGICA DE NAVIGARE: Conectarea Butonului "Rutina pe o zi"
        Button routineDayButton = findViewById(R.id.button_routine_day);

        if (routineDayButton != null) {
            routineDayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RoutineActivity.this, DayRoutineActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 2. Logică pentru Insets (pentru a aplica padding barei de jos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_routine_layout), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                // Aplicăm padding doar pe laterale și jos
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        // 3. LOGICA POP-UP MENIU (Cele 3 puncte) - DEZMUTARE ȘI RECONECTARE
        ImageButton popupMenuButton = findViewById(R.id.button_popup_menu_routine);
        if (popupMenuButton != null) {
            popupMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });
        }
    }

    // Metoda showPopupMenu rămâne aceeași
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_settings) {
                Toast.makeText(RoutineActivity.this, "Setări selectate (Rutină)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_about) {
                Toast.makeText(RoutineActivity.this, "Despre Aplicație selectat (Rutină)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_exit) {
                finish();
                return true;
            }
            return false;
        });
        popup.show();
    }
}