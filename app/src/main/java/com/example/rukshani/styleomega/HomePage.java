package com.example.rukshani.styleomega;

import android.os.Bundle;
import android.widget.ViewFlipper;

public class HomePage extends Navigation
       {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        onCreateMethod();
ViewFlipper viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper);
viewFlipper.setAutoStart(true);
viewFlipper.setFlipInterval(5000);
viewFlipper.startFlipping();
    }


}
