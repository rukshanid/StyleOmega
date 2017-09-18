package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

public class Checkout extends Navigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        onCreateMethod();

        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        Intent intent;
        if(username.equals("") || username.equals(null)){
            intent =new Intent(this,Login.class);
            startActivity(intent);
        }else {
            String query = "SELECT * FROM shopping_cart WHERE Username='" + username + "'";
            DBHelper db = null;
            try {
                db = new DBHelper(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Cursor rs = db.getData(query);
            int x = 50;
            int y = 50;
            LinearLayout linearLayoutParent = (LinearLayout) findViewById(R.id.linearlayout);
            TextView title = new TextView(this);
            title.setText("Checkout");
            title.setTextColor(Color.BLACK);
            title.setTextSize(25);
            linearLayoutParent.addView(title);
            Double subtotal = 0.0;
            if (rs.moveToFirst()) {
                do {
                    LinearLayout linearLayout = new LinearLayout(this);

                    LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    layoutParams0.setMargins(100, 100,100,200);

                    LinearLayout linearLayout2 = new LinearLayout(this);
                    linearLayout2.setLayoutParams(layoutParams0);
                    linearLayout2.setOrientation(LinearLayout.VERTICAL);


                    int drawableResourceId = this.getResources().getIdentifier(rs.getString(1).toLowerCase(), "drawable", this.getPackageName());
                    ImageView image = new ImageView(this);
                    TextView nameAndSize = new TextView(this);
                    TextView price = new TextView(this);




                    String query1 = "SELECT * FROM item WHERE ItemID='" + rs.getString(1).toString() + "'";
                    Cursor rs1 = db.getData(query1);
                    if (rs1.moveToFirst()) {
                        do {
                            image.setBackgroundResource(drawableResourceId);

                            nameAndSize.setText(rs1.getString(1).toString() + " "+rs.getString(2).toString());
                            Double doubleInstance = new Double(rs1.getDouble(5));
                            String priceValue = doubleInstance.toString();
                            price.setText(priceValue);
                            Integer integerInstance = new Integer(rs.getInt(3));
                            String qtyValue = integerInstance.toString();
                            Double total = doubleInstance * integerInstance;
                            String totalValue = total.toString();
                            price.setText(priceValue +" X "+qtyValue+" = "+totalValue);


                            subtotal = subtotal + total;

                        }while(rs1.moveToNext());
                    }rs1.close();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 360);
                    layoutParams.setMargins(20,20,20,20);
                    image.setLayoutParams(layoutParams);

                    image.setX(x);
                    image.setY(y);

                    linearLayoutParent.addView(linearLayout);
                    linearLayout.addView(image);
                    linearLayout.addView(linearLayout2);

                    linearLayout2.addView(nameAndSize);
                    linearLayout2.addView(price);
                    y += 20;
                } while (rs.moveToNext());

                final double total = subtotal;
                final Button pay = new Button(this);
                Button cancel = new Button(this);

                TextView subtotalValue = new TextView(this);
                subtotalValue.setText("\nSubtotal:\t     \t    \t     \t"+"     \t"+subtotal.toString()+0+"\n");
                subtotalValue.setTypeface(Typeface.DEFAULT_BOLD);
                subtotalValue.setTextSize(20);

                pay.setText("Pay");
                cancel.setText("Cancel");

                pay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            pay(total);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        goBackToCart();
                    }
                });

                //Payment Details
                EditText address = new EditText(this);
                EditText creditcard = new EditText(this);
                EditText security = new EditText(this);
                TextView addressText = new TextView(this);
                addressText.setText("SHIPPING ADDRESS: ");
                creditcard.setHint("Enter Your Credit Card Number Here");
                creditcard.setHintTextColor(getResources().getColor(R.color.colorAccent));
                creditcard.setInputType(InputType.TYPE_CLASS_NUMBER);
                security.setInputType(InputType.TYPE_CLASS_NUMBER);
                security.setHint("Enter the Security Code Here");
                security.setHintTextColor(getResources().getColor(R.color.colorAccent));

                //Address
                String sql = "SELECT * FROM users WHERE Username='"+username+"'";
                Cursor rs3 = db.getData(sql);
                if(rs3.moveToFirst()){
                    do{
                        address.setText(rs3.getString(5));
                    }while(rs3.moveToNext());
                }

                linearLayoutParent.addView(subtotalValue);
                linearLayoutParent.addView(addressText);
                linearLayoutParent.addView(address);
                linearLayoutParent.addView(creditcard);
                linearLayoutParent.addView(security);
                pay.setBackgroundColor(Color.BLACK);
                pay.setTextColor(Color.WHITE);
                cancel.setBackgroundResource(R.drawable.button_border);
                linearLayoutParent.addView(pay);
                linearLayoutParent.addView(cancel);
            }
            rs.close();
        }
    }
    public void pay(double total) throws IOException, SQLException {
        DBHelper db = new DBHelper(this);
        Sales sales = new Sales();
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        String salesID = sales.generateSalesID(db);


        String query = "SELECT * FROM shopping_cart WHERE Username='"+username+"'";
        Cursor rs = db.getData(query);

        if(rs.moveToFirst()){
            do{
                String itemID = rs.getString(1);
                String Size = rs.getString(2);
                int qty = rs.getInt(3);
                sales.newSale(username, salesID, total,itemID, Size, qty, db);
                sales.updateStock(itemID,Size,qty,db);

            }while(rs.moveToNext());
            //send email
            String query1 = "SELECT * FROM users WHERE Username='"+username+"'";
            Cursor rs1 = db.getData(query1);
            if(rs1.moveToFirst()){
                do {
                    EmailSender email = new EmailSender(this,
                            "Dear " + rs1.getString(2) + ",\n\nThank you for purchasing from Style Omega.  " +
                            "This is your confirmation ID: " + salesID + "\nRegards,\nStyle Omega.",
                            "Style Omega - Checkout", rs1.getString(6));
                    email.execute();
                }while (rs1.moveToNext());
            }
            deleteShoppingCartRecord(username,db);
        }rs.close();
        Intent intent = new Intent(this,Confirmation.class);
        startActivity(intent);
    }
    public void deleteShoppingCartRecord(String un,DBHelper db) throws SQLException {
        String query = "SELECT * from shopping_cart WHERE Username='"+un+"' ";
        Cursor rs = db.getData(query);
        if(rs.moveToFirst()){
            do {
                String query1 = "DELETE FROM `shopping_cart` WHERE Username='" + un + "'";
                db.executeSQL(query1);
            }while(rs.moveToNext());
        }

    }
    public void goBackToCart(){
        Intent intent = new Intent(this,ShoppingCart.class);
        startActivity(intent);
    }

}
