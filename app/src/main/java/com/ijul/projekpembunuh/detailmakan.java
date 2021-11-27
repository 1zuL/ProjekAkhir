package com.ijul.projekpembunuh;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class detailmakan extends AppCompatActivity {
    View ImageViewTitle;
    TextView TextViewRating,TextViewRelease;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailmakan);

        ImageViewTitle = findViewById(R.id.tvTitle);
        TextViewRating = findViewById(R.id.tvRating);
        TextViewRelease = findViewById(R.id.tvReleaseDate);


        makanbang maem = getIntent().getParcelableExtra("MOVIES");
        ImageViewTitle.findViewById(R.id.tvTitle);
        TextViewRating.setText(maem.getRating());
        TextViewRelease.setText(maem.getRelease());

    }
}