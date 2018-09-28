package com.example.hahay.photogallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MainActivity.filter = null;


    }

    //Filter List
    public void filterList(View view){
        TextInputEditText userInp = findViewById(R.id.searchInput);
        MainActivity.filter = userInp.getText().toString();
        Intent searchIntent = new Intent(this, MainActivity.class);
        startActivity(searchIntent);

    }
}
