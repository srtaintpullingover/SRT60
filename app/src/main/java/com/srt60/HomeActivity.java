package com.srt60;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Safely load layout if available, or fallback gracefully
        int layoutId = getResources().getIdentifier("activity_home", "layout", getPackageName());
        if (layoutId != 0) {
            setContentView(layoutId);
        }

        // Safely bind navigation views by name to avoid missing ID compilation errors
        String[] navIds = {"homeNavHome", "homeNavKeypad", "homeNavHistory"};
        for (int i = 0; i < navIds.length; i++) {
            int id = getResources().getIdentifier(navIds[i], "id", getPackageName());
            if (id != 0) {
                View navView = findViewById(id);
                if (navView != null) {
                    // Optional click setup or listeners can go here
                }
            }
        }
    }
}
