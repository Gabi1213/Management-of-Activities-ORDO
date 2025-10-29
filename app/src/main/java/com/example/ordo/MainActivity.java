package com.example.ordo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // NOU
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // NOU
import android.widget.PopupMenu; // NOU
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Logica Butonului Principal (Realizeaza Rutina)
        Button realizeazaRutinaButton = findViewById(R.id.button_realizeaza_rutina);
        realizeazaRutinaButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoutineActivity.class);
            startActivity(intent);
        });

        // 2. Logica Butonului de Meniu Pop-up (Cele 3 puncte)
        ImageButton popupMenuButton = findViewById(R.id.button_popup_menu);
        popupMenuButton.setOnClickListener(this::showPopupMenu); // Folosim o metodă separată pentru claritate

        // Codul existent pentru gestionarea Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Metoda care afișează meniul Pop-up când se apasă butonul cu 3 puncte.
     */
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        // Încarcă meniul din fișierul popup_menu.xml
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        // Setează acțiunea când este selectat un element din meniu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_settings) {
                    Toast.makeText(MainActivity.this, "Setări selectate", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_about) {
                    Toast.makeText(MainActivity.this, "Despre Aplicație selectat", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_exit) {
                    Toast.makeText(MainActivity.this, "Ieșire selectată", Toast.LENGTH_SHORT).show();
                    finish(); // Închide activitatea curentă
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }
}