package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        String itemID = intent.getStringExtra(DisplayItem.ITEM_ID);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.contactCL);
        int drawableResourceId = this.getResources().getIdentifier(itemID.toLowerCase(), "drawable", this.getPackageName());
        ImageView image = new ImageView(this);
        image.setBackgroundResource(drawableResourceId);
        image.setX(300);image.setY(350);
        LinearLayout.LayoutParams layoutParamsImg = new LinearLayout.LayoutParams(400,460);
        image.setLayoutParams(layoutParamsImg);
        constraintLayout.addView(image);

    }
    public void Inquire(View view) throws IOException {
        EditText enquiryValue = (EditText) findViewById(R.id.enquiry);
        String enquiry = enquiryValue.getText().toString();

        SessionManager session = new SessionManager(this);
        String username = session.getusername();

        DBHelper db = new DBHelper(this);
        String enquiryID = generateEnquiryID(db);
        Intent intent = getIntent();
        String itemID = intent.getStringExtra(DisplayItem.ITEM_ID);
        if(enquiry.equals("")){
            enquiryValue.setError("Enter your enquiry here!");

        }else {
            String query = "INSERT INTO enquiry (EnquiryID,Username,ItemID,Post) \n" +
                    "VALUES ('" +enquiryID+ "','" +username+ "','" +itemID+ "','" +enquiry+ "')";
            db.executeSQL(query);
            Toast.makeText(this, "Your question will be answered shortly await an email or check your inbox shortly...",
                    Toast.LENGTH_LONG).show();
           onBackPressed();
        }
    }

    public String generateEnquiryID(DBHelper db){
        String qry = "SELECT EnquiryID FROM enquiry";
        Cursor rs = db.getData(qry);
        if (rs.getCount()==0){
            String cid = "E1000";
            rs.close();
            return cid;
        }
        else{
            String query= "SELECT substr(EnquiryID,2,4)+1 AS EID FROM enquiry ORDER BY EnquiryID DESC LIMIT 1";
            Cursor rs1 = db.getData(query);
            if (rs1.moveToFirst()) {
                do {
                    String cid = "E" + rs1.getString(rs1.getColumnIndex("EID"));
                    return cid;
                } while (rs1.moveToNext());
            }
            rs.close();
            String cid = "E" + rs1.getString(rs1.getColumnIndex("EID"));
            rs1.close();
            return cid;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
