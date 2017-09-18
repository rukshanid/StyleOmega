package com.example.rukshani.styleomega;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Confirmation extends Navigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        onCreateMethod();
    }

    public void continueToShop(View view){
        Intent intent=new Intent(this,HomePage.class);
        startActivity(intent);
    }
}
