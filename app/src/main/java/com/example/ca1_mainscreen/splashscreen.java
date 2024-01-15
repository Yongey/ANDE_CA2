package com.example.ca1_mainscreen;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.activity_splashscreen);
        Thread splashThread = new Thread() {

            public void run() {
                try {
                    // sleep time in milliseconds (3000 = 3sec)
                    sleep(1500);
                }  catch(InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                } finally
                {
                    // Launch the MainActivity class
                    Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        // To Start the thread
        splashThread.start();
    }
}
