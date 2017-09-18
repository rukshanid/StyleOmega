package com.example.rukshani.styleomega;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class Inbox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        LinearLayout inboxLayout = (LinearLayout)findViewById(R.id.inboxLayout);
        String query="SELECT * FROM enquiry WHERE Username='"+username+"' AND Reply!=''";
        TextView title = new TextView(this);
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setText("Inbox");
        inboxLayout.addView(title);

        DBHelper db = null;
        try {
            db = new DBHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Cursor rs = db.getData(query);
        if(rs.moveToFirst()){
            do{
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);

                inboxLayout.addView(itemLayout);

                LinearLayout postLayout = new LinearLayout(this);
                postLayout.setOrientation(LinearLayout.VERTICAL);

                String itemID = rs.getString(2);
                int drawableResourceId = this.getResources().getIdentifier(itemID.toLowerCase(),
                        "drawable", this.getPackageName());
                ImageView image = new ImageView(this);
                image.setBackgroundResource(drawableResourceId);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 260);
                image.setLayoutParams(layoutParams);

                TextView post = new TextView(this);
                post.setText("Question:\n"+rs.getString(3));

                TextView reply = new TextView(this);
                reply.setText("Reply:\n"+rs.getString(4));

                itemLayout.addView(image);
                itemLayout.addView(postLayout);
                postLayout.addView(post);
                postLayout.addView(reply);

            }while(rs.moveToNext());
        }else {
            TextView msg = new TextView(this);
            msg.setText("Inbox is Empty");
            inboxLayout.addView(msg);
        }rs.close();

    }
}
