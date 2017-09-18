package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class DisplayItems extends Navigation{

    public static final String ITEM_ID = "com.example.rukshani.itemid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);
        onCreateMethod();
        Intent intent = getIntent();
        String type = intent.getStringExtra(Navigation.VALUE);
        String category = intent.getStringExtra(Navigation.CATEGORY);

        TextView title = new TextView(this);
        title.setTextColor(Color.BLACK);
        title.setTextSize(24);
        title.setText("\n\t   "+category+" "+type);

        DBHelper db = null;
        try {
            db = new DBHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sql = "SELECT * FROM item WHERE Type='"+type+"' AND Category='"+category+"'";
        Cursor rs = db.getData(sql);

        if (rs.moveToFirst()) {

            LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearlayoutDisplayItems);
            linearLayout1.addView(title);


            do {
                final String itemID = rs.getString(0).toString();
                String name = rs.getString(1).toString();
                double price = rs.getDouble(5);
                String qry = "SELECT * FROM item_stock WHERE ItemID='" + itemID + "'";
                Cursor rs1 = db.getData(qry);

                LinearLayout linear = new LinearLayout(this);
                linear.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout1.addView(linear);

                int totalQ = 0;
                if (rs1.moveToNext()) {
                    do {
                        totalQ = totalQ + (Integer) rs1.getInt(2);
                    } while (rs1.moveToNext());
                }
                rs1.close();
                if (totalQ > 0) {
                    //set image
                    int drawableResourceId = this.getResources().getIdentifier(itemID.toLowerCase(), "drawable", this.getPackageName());
                    ImageView image = new ImageView(this);
                    image.setBackgroundResource(drawableResourceId);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 560);
                    layoutParams.setMargins(20,20,20,20);
                    image.setLayoutParams(layoutParams);

                    //set price and name
                    TextView text = new TextView(this);
                    text.setText("\t    "+name.toUpperCase()+"\n\t    Rs."+price+0);

                    text.setClickable(true);
                    text.setY(200);
                    text.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            goToDisplayItem(itemID);
                        }
                    });

                    image.setClickable(true);
                    image.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            goToDisplayItem(itemID);
                        }
                    });

                    linear.addView(image);
                    linear.addView(text);
                }

            } while (rs.moveToNext());
        }
        rs.close();
    }
    public void goToDisplayItem(String itemID){
        Intent intent = new Intent(this,DisplayItem.class);
        intent.putExtra(ITEM_ID,itemID);;
        startActivity(intent);
    }
}
