package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class SearchResults extends Navigation {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        onCreateMethod();
        Intent intent = getIntent();
        String searchQ = intent.getStringExtra("SEARCH");
        search(searchQ);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void search(String searchQuery){

        String query = "SELECT * FROM item WHERE (' '|| Category || ' ') LIKE '% "+searchQuery+" %' OR\n" +
                "(' ' || Type || ' ') LIKE '% "+searchQuery+" %' OR\n" +
                "(' ' || Name || ' ') LIKE '% "+searchQuery+" %' OR\n" +
                "(' ' || Description ||' ') LIKE '% "+searchQuery+" %' OR\n" +
                "(' ' || Price || ' ') LIKE '% "+searchQuery+" %' OR " +
                "(' '|| Category || ' '||Type|| ' ') LIKE '% "+searchQuery+" %'";

        DBHelper db = null;
        try {
            db = new DBHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutParent);
        TextView title = new TextView(this);
        title.setText("\tSearch results for '"+searchQuery+"'\n");
        title.setTextSize(16);
        linearLayout.addView(title);
        Cursor rs = db.getData(query);
        if(rs.moveToFirst()){
             do{
                final String itemID = rs.getString(0);
                String name = rs.getString(1);

                LinearLayout linearLayout1 = new LinearLayout(this);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(linearLayout1);
                TextView textView = new TextView(this);

                int drawableResourceId = this.getResources().getIdentifier(rs.getString(0).toLowerCase(), "drawable", this.getPackageName());
                ImageView image = new ImageView(this);
                image.setBackgroundResource(drawableResourceId);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 560);
                layoutParams.setMargins(20, 20, 20, 20);
                image.setLayoutParams(layoutParams);
                image.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        displayItem(itemID);
                    }
                });

                textView.setText(name + " \n" + rs.getDouble(5));

                linearLayout1.addView(image);
                linearLayout1.addView(textView);
            }while(rs.moveToNext());

        }

        else {
            TextView textView = new TextView(this);
            textView.setText("NO RESULTS FOR '"+searchQuery.toUpperCase()+"'");
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            linearLayout.addView(textView);
        }


    }
    public static final String ITEM_ID = "com.example.rukshani.itemid";

    public void displayItem(String itemID){
        Intent intent = new Intent(this,DisplayItem.class);
        intent.putExtra(ITEM_ID,itemID);;
        startActivity(intent);
    }
}
