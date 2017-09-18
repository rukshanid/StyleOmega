package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static java.lang.String.valueOf;

public class ShoppingCart extends Navigation {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        onCreateMethod();

        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        Intent intent;
        if(username.equals("") || username.equals(null)){
            intent =new Intent(this,Login.class);
            startActivity(intent);
        }else{
            String query = "SELECT * FROM shopping_cart WHERE Username='"+username+"'";
            DBHelper db= null;
            try {
                db = new DBHelper(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Cursor rs = db.getData(query);
            int x=50;
            int y=150;
            LinearLayout linearLayoutParent = (LinearLayout)findViewById(R.id.linearlayout);
            TextView title = new TextView(this);
            title.setText("Shopping Cart");
            title.setTextColor(Color.BLACK);
            title.setTextSize(25);
            linearLayoutParent.addView(title);
            Double subtotal = 0.0;
            if(rs.moveToFirst()){
                do{//set the sc pics to links for items
                    String query2 = "SELECT * FROM item_stock WHERE ItemID='"+rs.getString(1).toString()+"' " +
                            "AND Size='"+rs.getString(2).toString()+"'";
                    Cursor rs2 = db.getData(query2);
                    if(rs2.moveToFirst()) {
                        do {
                            if (rs2.getInt(2) > 0) { //Quantity > 0

                                String query1 = "SELECT * FROM item WHERE ItemID='" + rs.getString(1).toString() + "'";
                                Cursor rs1 = db.getData(query1);
                                LinearLayout linearLayout = new LinearLayout(this);

                                LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                layoutParams0.setMargins(100, 50, 50, 50);

                                LinearLayout linearLayout2 = new LinearLayout(this);
                                linearLayout2.setLayoutParams(layoutParams0);
                                linearLayout2.setOrientation(LinearLayout.VERTICAL);


                                int drawableResourceId = this.getResources().getIdentifier(rs.getString(1).toLowerCase(), "drawable", this.getPackageName());
                                ImageView image = new ImageView(this);
                                TextView name = new TextView(this);

                                TextView size = new TextView(this);
                                TextView update = new TextView(this);
                                TextView price = new TextView(this);
                                final EditText qty = new EditText(this);
                                qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                                TextView totalText = new TextView(this);
                                Button removeBtn = new Button(this);

                                if (rs1.moveToFirst()) {
                                    do {
                                        final String itemID=rs.getString(1).toString();
                                        final String sizeV = rs.getString(2).toString();
                                        image.setBackgroundResource(drawableResourceId);
                                        name.setText(rs1.getString(1).toString());
                                        size.setText("Size: "+rs.getString(2).toString());
                                        String priceValue = valueOf(rs1.getDouble(5));
                                        price.setText("Price: "+priceValue);

                                        image.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                displayItem(itemID);
                                            }
                                        });

                                        LinearLayout.LayoutParams layoutParamsQty = new LinearLayout.LayoutParams(130,100);
                                        String qtyValue = valueOf(rs.getInt(3));
                                        qty.setLayoutParams(layoutParamsQty);
                                        qty.setText(qtyValue);


                                        update.setText("\tUpdate");

                                        Double total = rs1.getDouble(5) * rs.getInt(3);
                                        String totalValue = total.toString();
                                        totalText.setText("Total: "+totalValue);
                                        removeBtn.setText("REMOVE");
                                        removeBtn.setTextSize(14);
                                        removeBtn.setTextColor(Color.WHITE);
                                        removeBtn.setBackgroundColor(Color.BLACK);


                                        removeBtn.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {

                                                try {
                                                    removeFromShoppingCart(itemID,sizeV);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        //final String quantity =qty.getEditableText().toString();;
                                        update.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {

                                                try {

                                                    updateShoppingCart(itemID,sizeV,qty);

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        subtotal = subtotal+total;

                                    } while (rs1.moveToNext());
                                }
                                rs1.close();

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300,360);
                                image.setLayoutParams(layoutParams);

                                image.setX(x);
                                image.setY(y);

                                linearLayoutParent.addView(linearLayout);
                                linearLayout.addView(image);

                                LinearLayout quantityLayout = new LinearLayout(this);
                                quantityLayout.setOrientation(LinearLayout.HORIZONTAL);
                                TextView q = new TextView(this);
                                q.setText("Quantity: ");
                                linearLayout.addView(linearLayout2);
                                linearLayout2.addView(name);
                                linearLayout2.addView(size);
                                linearLayout2.addView(price);
                                linearLayout2.addView(quantityLayout);
                                quantityLayout.addView(q);
                                quantityLayout.addView(qty);
                                update.setTypeface(Typeface.DEFAULT_BOLD);
                                update.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                update.setTextColor(Color.BLACK);
                                quantityLayout.addView(update);
                                linearLayout2.addView(totalText);
                                linearLayout2.addView(removeBtn);

                                y += 20;
                            }else{
                                String query3 = "DELETE FROM shopping_cart WHERE ItemID='"+rs.getString(1).toString()+"'" +
                                        " AND Size='"+rs.getString(2).toString()+"'";
                                db.executeSQL(query3);
                            }}
                            while (rs2.moveToNext()) ;
                        }rs2.close();
                }while(rs.moveToNext());

                final Button checkout = new Button(this);
                TextView subtotalText =new TextView(this);
                checkout.setTextColor(Color.WHITE);
                checkout.setBackgroundColor(Color.BLACK);
                String subtotalString = subtotal.toString();
                subtotalText.setText("Subtotal:\t\t\t\t                           "+subtotalString+0);
                subtotalText.setTypeface(Typeface.DEFAULT_BOLD);
                subtotalText.setTextSize(20);
                checkout.setText("CHECKOUT");

                checkout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        goToCheckout();
                    }
                });

                linearLayoutParent.addView(subtotalText);
                linearLayoutParent.addView(checkout);

            }else{
                TextView msg = new TextView(this);
                msg.setText("Shopping Cart is Empty");
                linearLayoutParent.addView(msg);
            }rs.close();
        }
    }
    public void removeFromShoppingCart(String id,String size) throws IOException {
        DBHelper db =new DBHelper(this);
        SessionManager session=new SessionManager(this);
        String un = session.getusername();
        String query ="DELETE FROM shopping_cart WHERE ItemID='"+id+"' AND Username='"+un+"' AND Size='"+size+"'";
        db.executeSQL(query);
        Intent intent = new Intent(this,ShoppingCart.class);
        startActivity(intent);
    }

    public void updateShoppingCart(String itemID,String size,EditText et) throws IOException {

        DBHelper db = new DBHelper(this);
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        if (et.getText().toString().equals("")) {
            et.setError("Quantity is required");
        }else if(Integer.parseInt(et.getText().toString())<=0){
            et.setError("Quantity should be greater than 0");
        }
        else{
        int qty = Integer.parseInt(et.getText().toString());

        String query2 = "SELECT * FROM item_stock WHERE ItemID='" + itemID + "' AND Size='" + size + "'";
        Cursor rs2 = db.getData(query2);
        if (rs2.moveToFirst()) {
            do {
                if (rs2.getInt(2) < qty) {
                    et.setError("Quantity entered is more than stock available please enter less than " + rs2.getInt(2));
                } else {
                    String query1 = "UPDATE shopping_cart SET Quantity='"+qty+"' WHERE Username='"+username+"' " +
                            "AND ItemID='"+itemID+"' AND Size='"+size+"'";
                    db.executeSQL(query1);
                    Toast.makeText(this, "Quantity updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ShoppingCart.class);
                    startActivity(intent);
                }
            } while (rs2.moveToNext());
        }
    }
    }



    public void goToCheckout(){
        Intent intent = new Intent(this,Checkout.class);
        startActivity(intent);
    }

    public static final String ITEM_ID = "com.example.rukshani.itemid";

    public void displayItem(String itemID){
        Intent intent = new Intent(this,DisplayItem.class);
        intent.putExtra(ITEM_ID,itemID);;
        startActivity(intent);
    }
}
