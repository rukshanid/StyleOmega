package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;

public class Review extends AppCompatActivity{

    public static final String ITEM_ID = "com.example.rukshani.itemid";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();
        String itemID = intent.getStringExtra(DisplayItem.ITEM_ID);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.reviewCL);

        int drawableResourceId = this.getResources().getIdentifier(itemID.toLowerCase(), "drawable", this.getPackageName());
        ImageView image = new ImageView(this);
        image.setBackgroundResource(drawableResourceId);
        image.setX(300);image.setY(250);
        LinearLayout.LayoutParams layoutParamsImg = new LinearLayout.LayoutParams(400,460);
        image.setLayoutParams(layoutParamsImg);
        constraintLayout.addView(image);
    }

    public void addReview(View view) throws IOException {
        EditText reviewValue = (EditText) findViewById(R.id.review);
        String review = reviewValue.getText().toString();
        RatingBar rate  =(RatingBar)findViewById(R.id.ratingBar);

        Float rating = rate.getRating();
        SessionManager session = new SessionManager(this);
        String username = session.getusername();

        DBHelper db = new DBHelper(this);
        String reviewID = generateReviewID(db);
        Intent intent = getIntent();
        String itemID = intent.getStringExtra(DisplayItem.ITEM_ID);
        if(rating==0 && review.equals("")){
            Toast.makeText(this,"Select a level on the rating bar or add a review",Toast.LENGTH_LONG).show();
        }else {
            String query = "INSERT INTO review (ReviewID,ItemID,Username,Review,Rating) \n" +
                    "VALUES ('" + reviewID + "','" + itemID + "','" + username + "','" + review + "','" + rating + "')";
            db.executeSQL(query);
            Toast.makeText(this, "Successfully Uploaded Review!", Toast.LENGTH_LONG).show();
            intent = new Intent(getApplicationContext(),DisplayItem.class);
            intent.putExtra(ITEM_ID,itemID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public String generateReviewID(DBHelper db){
        String qry = "SELECT ReviewID FROM review";
        Cursor rs = db.getData(qry);
        if (rs.getCount()==0){
            String cid = "R1000";
            rs.close();
            return cid;
        }
        else{
            String query= "SELECT substr(ReviewID,2,4)+1 AS RID FROM review ORDER BY ReviewID DESC LIMIT 1";
            Cursor rs1 = db.getData(query);
            if (rs1.moveToFirst()) {
                do {
                    String cid = "R" + rs1.getString(rs1.getColumnIndex("RID"));
                    return cid;
                } while (rs1.moveToNext());
            }
            rs.close();
            String cid = "R" + rs1.getString(rs1.getColumnIndex("RID"));
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
