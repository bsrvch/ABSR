package com.example.absr;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class StartActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        Network n = new Network();
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
