package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ModifyAccount extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);
        DBHelper db = null;
        try {
            db = new DBHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        String sql = "SELECT * FROM users WHERE Username='"+username+"'";
        Cursor rs = db.getData(sql);
        if (rs.moveToFirst()){
            do{
                EditText firstName= (EditText) findViewById(R.id.firstName);
                EditText lastName= (EditText) findViewById(R.id.lastName);
                EditText contact= (EditText) findViewById(R.id.contactNumber);
                EditText address= (EditText) findViewById(R.id.address);
                EditText email= (EditText) findViewById(R.id.email);
                firstName.setText(rs.getString(2));
                lastName.setText(rs.getString(3));
                contact.setText(rs.getString(4));
                address.setText(rs.getString(5));
                email.setText(rs.getString(6));
            }while(rs.moveToNext());
        }
        rs.close();
    }

    public void modify(View view) throws IOException {
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText contact = (EditText) findViewById(R.id.contactNumber);
        EditText address = (EditText) findViewById(R.id.address);
        EditText email = (EditText) findViewById(R.id.email);
        String firstN = firstName.getText().toString();
        String lastN = lastName.getText().toString();
        String contactNumber = contact.getText().toString();
        String shippingAddress = address.getText().toString();
        String emailAddress = email.getText().toString();
        DBHelper db = new DBHelper(this);
        String query = "SELECT * from users WHERE Email='"+emailAddress+"' AND Username!='"+username+"'";
        Cursor rs = db.getData(query);
        if (rs.moveToFirst()) {
            do {
                email.setError("Email Already in use, Try another email");
            } while (rs.moveToNext());
        }else if (emailAddress.equals("") ){
                email.setError("Email is required");
        }else if(firstN.equals("")){
            firstName.setError("First name is required");
        }else if(shippingAddress.equals("")){
            address.setError("Address is required");
        }else if(lastN.equals("")) {
            lastName.setError("Last name is required");
        }else if (!(emailAddress.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}"))) {
                email.setError("Email Format Incorrect");
        }else {
            String query1 ="UPDATE users SET FirstName='"+firstN+"',LastName='"+lastN+"',ContactNumber='"+contactNumber+"',Address='"+shippingAddress+"',"
                    + "Email='"+emailAddress+"' where Username='"+username+"'";
            db.executeSQL(query1);
            Toast.makeText(this, "Successfully updated your profile details", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,MyAccount.class);
            startActivity(intent);
        }
        rs.close();
    }
}
