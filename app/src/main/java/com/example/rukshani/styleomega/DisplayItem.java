package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class DisplayItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        Intent intent = getIntent();
        final String itemID = intent.getStringExtra(ITEM_ID);

        DBHelper db =null;
        try {
            db = new DBHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sql ="SELECT * FROM item WHERE ItemID='"+itemID+"'";
        Cursor rs = db.getData(sql);

        if(rs.moveToFirst()) {
            do {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_Item);
                LinearLayout linearLayoutDesc = (LinearLayout) findViewById(R.id.linearLayout_ItemDesc);
                LinearLayout linearLayoutSpinner= (LinearLayout) findViewById(R.id.linearLayout_Spinner);
                LinearLayout linearLayoutE = (LinearLayout) findViewById(R.id.linearLayout_enquiries);
                LinearLayout linearLayoutR = (LinearLayout) findViewById(R.id.linearLayout_reviews);

                //Item Image
                int drawableResourceId = this.getResources().getIdentifier(itemID.toLowerCase(),
                        "drawable", this.getPackageName());
                ImageView image = new ImageView(this);
                image.setBackgroundResource(drawableResourceId);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,560);
                layoutParams.setMargins(50,50,50,50);
                image.setLayoutParams(layoutParams);
                image.setX(20);
                image.setY(70);

                //Quantity Validation
                String sql1 ="SELECT * FROM item_stock WHERE ItemID='"+itemID+"' AND Quantity>0";
                Cursor rs1 = db.getData(sql1);

                //Size values
                TextView sizeValues= new TextView(this);
                String sizes="";

                int qty=0;
                final Spinner sizeSpinner = new Spinner(this);
                ArrayList<String> sizeList = new ArrayList<String>();
                String type =rs.getString(3);

                //set size according to clothing item type
                if(!(type.equals("Accessories") || type.equals("Bags") || type.equals("SchoolSupplies"))) {
                    sizeList.add("Select Size*");
                }else{
                    sizeList.add("F");
                }
                TextView totalQ = new TextView(this);

                //get sizes and stock values
                if(rs1.moveToFirst()) {
                    do {
                        sizeList.add(rs1.getString(1).toString());
                        sizes =sizes+ rs1.getString(1).toString()+ " ";
                        qty =qty+rs1.getInt(2);
                    }while(rs1.moveToNext());
                    totalQ.setText("Available Quantity: "+qty);
                }

                //Size Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, sizeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sizeSpinner.setAdapter(adapter);

                sizeValues.setText("Available Sizes: "+ sizes);

                //Share and Shopping Cart Buttons creation
                Button share = new Button(this);
                share.setText("SHARE");
                Button shoppingCart =new Button(this);
                shoppingCart.setText("ADD TO CART");

                //Shopping Cart Button
               shoppingCart.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            addToShoppingCart(itemID, sizeSpinner.getSelectedItem().toString(),sizeSpinner);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //Creation of purchase,review and contact buttons
                Button purchase= new Button(this);
                purchase.setText("BUY IT NOW");
                Button addReview = new Button(this);
                Button contact = new Button(this);
                addReview.setText("REVIEW ITEM");
               contact.setText("INQUIRE");

                //review button
                addReview.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
goToReview(itemID);
                    }
                });

                //contact button
                contact.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
goToContact(itemID);
                    }
                });


                //Number Picker
                final NumberPicker quantity = new NumberPicker(this);
                LinearLayout.LayoutParams layoutParamNP = new LinearLayout.LayoutParams(300,400);
                layoutParamNP.setMargins(0,50,0,0);
                quantity.setLayoutParams(layoutParamNP);
                quantity.setMinValue(1);
                quantity.setMaxValue(qty);
                quantity.setWrapSelectorWheel(false);

                //Purchase Button
                purchase.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        try {
                            purchase(quantity.getValue(), sizeSpinner.getSelectedItem().toString(), itemID);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


                //Enquiries
                String query = "SELECT * FROM enquiry WHERE ItemID='"+itemID+"' AND reply!=''";
                Cursor rs2 =db.getData(query);
                TextView titleEnquiries=new TextView(this);
                titleEnquiries.setText("\nENQUIRIES"+"\n");
                titleEnquiries.setTypeface(Typeface.DEFAULT_BOLD);
                linearLayoutE.addView(titleEnquiries);

                if(rs2.moveToFirst()){
                    do{
                        TextView enquiry = new TextView(this);
                        TextView reply = new TextView(this);
                        enquiry.setText("Enquiry: "+ rs2.getString(3));
                        reply.setText("\tReply: "+rs2.getString(4)+"\n");
                        linearLayoutE.addView(enquiry);
                        linearLayoutE.addView(reply);
                    }while(rs2.moveToNext());
                }else{
                    TextView msg = new TextView(this);
                    msg.setText("No enquiries");
                    linearLayoutE.addView(msg);
                }rs2.close();

                //Reviews
                String query1 = "SELECT * FROM review WHERE ItemID='"+itemID+"'";
                Cursor rs3 =db.getData(query1);
                TextView titleReviews=new TextView(this);
                titleReviews.setText("REVIEWS"+"\n");
                titleReviews.setTypeface(Typeface.DEFAULT_BOLD);
                linearLayoutR.addView(titleReviews);
                if(rs3.moveToFirst()){
                    do{
                        TextView username = new TextView(this);
                        TextView review = new TextView(this);

                            username.setText(rs3.getString(2));

                            review.setText("\t" + rs3.getString(3));

                        linearLayoutR.addView(username);
                    if(!rs3.getString(3).equals("")) {
                        linearLayoutR.addView(review);
                    }
                        LinearLayout stars = new LinearLayout(this);
                        stars.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayoutR.addView(stars);

                        TextView space = new TextView(this);
                        space.setText("\t");
                        stars.addView(space);
                        int rate = Integer.parseInt(rs3.getString(4));
                        for(int i =0;i<rate;i++){
                            ImageView img =new ImageView(this);
                            LinearLayout.LayoutParams layoutParamsImg = new LinearLayout.LayoutParams(40,40);
                            img.setLayoutParams(layoutParamsImg);
                            img.setImageResource(R.drawable.star);
                            stars.addView(img);
                        }
                    }while(rs3.moveToNext());
                }else{
                    TextView msg = new TextView(this);
                    msg.setText("No Reviews");
                    linearLayoutR.addView(msg);
                }rs3.close();


                //Item Name
                final TextView name = new TextView(this);
                name.setText(rs.getString(1));
                name.setTextColor(Color.BLACK);
                name.setTextSize(18);
                linearLayoutDesc.addView(name);

                //Descriptions
                TextView description = new TextView(this);
                description.setText("Description: "+rs.getString(4));
                TextView price = new TextView(this);
                price.setText("Price: "+rs.getDouble(5));
                linearLayoutDesc.addView(description);
                linearLayoutDesc.addView(price);
                linearLayoutDesc.addView(totalQ);

                if(!(type.equals("Accessories") || type.equals("Bags") || type.equals("SchoolSupplies"))) {
                    linearLayoutDesc.addView(sizeValues);
                }


                linearLayoutSpinner.addView(image);

                LinearLayout linear = new LinearLayout(this);
                linear.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                linear.setLayoutParams(params);
                linearLayoutSpinner.addView(linear);
                TextView quantityLabel = new TextView(this);
                quantityLabel.setText("\n\tQuanitity:");
                linear.addView(quantityLabel);
                linear.addView(quantity);

                if(!(type.equals("Accessories") || type.equals("Bags") || type.equals("SchoolSupplies"))) {
                    linearLayout.addView(sizeSpinner);
                }

                //Buttons UI
                linear.setLayoutParams(params);
                shoppingCart.setBackgroundColor(Color.BLACK);
                shoppingCart.setTextColor(Color.WHITE);
                purchase.setBackgroundResource(R.drawable.button_border);
                addReview.setBackgroundColor(Color.BLACK);
                addReview.setTextColor(Color.WHITE);
                contact.setBackgroundResource(R.drawable.button_border);
                share.setBackgroundColor(Color.BLACK);
                share.setTextColor(Color.WHITE);
                //Share button
                share.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Look at this "+name.getText().toString()+" " +
                                "on Style Omega http://styleomega.com";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    }
                });

                //Buttons
                linearLayout.addView(shoppingCart);
                linearLayout.addView(purchase);
                linearLayout.addView(addReview);
                linearLayout.addView(contact);
                linearLayout.addView(share);

            }while(rs.moveToNext());
            }
            rs.close();


    }

    public void addToShoppingCart(String itemID,String size,Spinner sizeSpinner) throws IOException {
        SessionManager session = new SessionManager(this);
        String username =session.getusername();
        Intent intent = new Intent(this,Login.class);
        DBHelper db = new DBHelper(this);
        String sizeValue = sizeSpinner.getSelectedItem().toString();
        String query ="SELECT * FROM shopping_cart WHERE Username='"+username+"' AND ItemID='"+itemID+"' " +
                "AND Size='"+size+"'";
        Cursor rs = db.getData(query);
        if (username==null|| username==""){
            Toast.makeText(this,"Please Login to add items to the shopping cart",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        else if(rs.moveToFirst()){
            do {
                Toast.makeText(this,"Item with selected size is already in the shopping cart",Toast.LENGTH_LONG).show();
            }while(rs.moveToNext());
        }else if (sizeValue.equals("Select Size*") || sizeValue.equals(null)){
             Toast.makeText(this,"Please select a size",Toast.LENGTH_SHORT).show();
        }
        else {
            int qty =1;
            String query1 = "INSERT INTO shopping_cart(`Username`,`ItemID`,`Size`,`Quantity`) " +
                    "VALUES('"+username+"','"+itemID+"','"+size+"','"+qty+"')";
            db.executeSQL(query1);
            Toast.makeText(this,"Item added to the Shopping cart",Toast.LENGTH_LONG).show();

        }
    }
    public static final String ItemID = "com.example.rukshani.itemID";
    public static final String Size= "com.example.rukshani.Size";
    public static final String Quantity= "com.example.rukshani.Quantity";
    public void purchase(int qty,String size,String itemID) throws IOException {
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        DBHelper db = new DBHelper(this);
        String sql = "SELECT * FROM item_stock WHERE itemID='"+itemID+"' AND Size='"+size+"'";
        Cursor rs=db.getData(sql);
        if(username.equals("")){
            Toast.makeText(this,"Please Login to Purchase an Item",Toast.LENGTH_LONG).show();
            Intent intent= new Intent(this,Login.class);
            startActivity(intent);
        }else {
            if (size.equals("Select Size*") || size.equals(null)) {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_LONG).show();
            }
            if (rs.moveToFirst()) {
                do {
                    if (qty > rs.getInt(2)) {
                        Toast.makeText(this, "Enter a quantity less than " + rs.getInt(2), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(this, Purchase.class);
                        intent.putExtra(ItemID, itemID);
                        intent.putExtra(Size, size);
                        String quantity = valueOf(qty);
                        intent.putExtra(Quantity, quantity);
                        startActivity(intent);
                    }
                } while (rs.moveToNext());
            }
            rs.close();
        }
    }
    public static final String ITEM_ID = "com.example.rukshani.itemid";
    public void goToReview(String itemID){
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        if(username.equals("")){
            Toast.makeText(this,"Please login to upload a review",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, Review.class);
            intent.putExtra(ITEM_ID, itemID);
            startActivity(intent);
        }

    }

    public void goToContact(String itemID){
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        if(username.equals("")){
            Toast.makeText(this,"Please login to contact Style Omega about this item",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, Contact.class);
            intent.putExtra(ITEM_ID, itemID);
            startActivity(intent);
        }

    }

}
