package com.ijul.projekpembunuh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class Dasboard extends AppCompatActivity {
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    ImageView keranjang, masuklogin, fitnes, menu;
    AdapterMakanBang adapter;
    ArrayList<makanbang> objMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        recyclerView = findViewById(R.id.recycleView);
        objMovies.add(new makanbang (R.drawable.salad, "Salad Buah", "Salad Buah Cocok Buat siang Hari"));
        objMovies.add(new makanbang(R.drawable.buah2,"Nasi Goreng", "Buat apa Diet BRoo, Makan Aku aja"));
        objMovies.add(new makanbang(R.drawable.buah1,"Sate Ayam", "Iyaa.. Buat apa Diet, Sini sama aku"));

        adapter = new AdapterMakanBang(objMovies);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Dasboard.this));


        int images[] = {R.drawable.buah1, R.drawable.buah2, R.drawable.buah3, R.drawable.buah4};
        viewFlipper = findViewById(R.id.flipper);

        for (int image : images) {
            flipperImages(image);
        }

        fitnes = findViewById(R.id.fitnes);
        fitnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dasboard.this, Sples.class));
            }
        });


        keranjang = findViewById(R.id.keranjang);
        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dasboard.this, Cart.class));
            }
        });


        masuklogin = findViewById(R.id.masuklogin);
        masuklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dasboard.this, MainActivity.class));
            }
        });

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dasboard.this, MenuMakan.class));
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

    }

};
