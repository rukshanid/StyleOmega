package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

public class Purchase extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        Intent intent = getIntent();
        final String itemID = intent.getStringExtra(DisplayItem.ItemID);
        final String size = intent.getStringExtra(DisplayItem.Size);
        final String qty = intent.getStringExtra(DisplayItem.Quantity);

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout);


        int drawableResourceId = this.getResources().getIdentifier(itemID.toLowerCase(), "drawable", this.getPackageName());
        ImageView image = new ImageView(this);
        TextView sizeDetails = new TextView(this);
        TextView otherDetails = new TextView(this);
        TextView price = new TextView(this);
        Button pay = new Button(this);
        Button cancel = new Button(this);

        String query ="SELECT * FROM item WHERE ItemID='"+itemID+"'";

        DBHelper db = null;
        try {
            db = new DBHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
       double total =0;
        Cursor rs = db.getData(query);
        if(rs.moveToFirst()){
            do{
                otherDetails.setText("\n\n"+rs.getString(1));
                int quantity = Integer.parseInt(qty);
                total = rs.getDouble(5)*quantity;

                price.setText("Total:\t"+rs.getDouble(5)+" x "+quantity+" = "+total+0+"\n");
            }while (rs.moveToNext());
        }rs.close();
        final double totalValue= total;
        sizeDetails.setText("Size: "+size);
        pay.setText("PAY");
        cancel.setText("CANCEL");
        image.setBackgroundResource(drawableResourceId);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400,460);
        layoutParams.setMargins(0,0,0,20);
        image.setLayoutParams(layoutParams);

        pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(qty);
                try {

                    pay(itemID,size,quantity,totalValue);
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
                onBackPressed();
            }
        });

        image.setX(310);
        image.setY(70);
        otherDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sizeDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        linearLayout1.addView(image);
        linearLayout1.addView(otherDetails);
        linearLayout1.addView(sizeDetails);
        linearLayout1.addView(price);
        TextView addressText = new TextView(this);
        addressText.setText("SHIPPING ADDRESS: ");
        EditText address = new EditText(this);
        EditText creditcard = new EditText(this);
        EditText security = new EditText(this);
        creditcard.setHint("Enter Your Credit Card Number Here");
        creditcard.setHintTextColor(getResources().getColor(R.color.colorAccent));
        security.setHint("Enter the Security Code Here");
        security.setHintTextColor(getResources().getColor(R.color.colorAccent));

        creditcard.setInputType(InputType.TYPE_CLASS_NUMBER);
        security.setInputType(InputType.TYPE_CLASS_NUMBER);
        String sql = "SELECT * FROM users WHERE Username='"+username+"'";
        Cursor rs3 = db.getData(sql);
        if(rs3.moveToFirst()){
            do{
                address.setText(rs3.getString(5));
            }while(rs3.moveToNext());
        }
        pay.setTextColor(Color.WHITE);
        pay.setBackgroundColor(Color.BLACK);
        cancel.setBackgroundResource(R.drawable.button_border);
        linearLayout1.addView(addressText);
        linearLayout1.addView(address);
        linearLayout1.addView(creditcard);
        linearLayout1.addView(security);
        linearLayout1.addView(pay);
        linearLayout1.addView(cancel);

    }

    public void pay(String itemID,String size,int qty,double total) throws IOException, SQLException {
        DBHelper db = new DBHelper(this);
        Sales sales = new Sales();
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        String salesID = sales.generateSalesID(db);
        sales.newSale(username, salesID, total,itemID, size, qty, db);
        sales.updateStock(itemID,size,qty,db);
        String query1 = "SELECT * FROM users WHERE Username='"+username+"'";
        Cursor rs1 = db.getData(query1);
        if(rs1.moveToFirst()) {
            do{
                EmailSender email = new EmailSender(this, "Dear " + rs1.getString(2) +
                        ",\n\nThank you for purchasing from Style Omega.  " +
                        "This is your confirmation ID: " + salesID + "\nRegards,\nStyle Omega.",
                        "Style Omega - Purchase", rs1.getString(6));
                email.execute();
            }while (rs1.moveToNext());
        }
        Intent intent = new Intent(this,Confirmation.class);
        startActivity(intent);
    }

}
