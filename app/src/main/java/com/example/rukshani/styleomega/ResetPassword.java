package com.example.rukshani.styleomega;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ResetPassword extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

    }

    public void resetPassword(View view) throws IOException {
        SessionManager session = new SessionManager(this);
        String username = session.getusername();
        EditText cpwd = (EditText) findViewById(R.id.currentPassword);
        String currentPwd = cpwd.getText().toString();
        EditText npwd = (EditText) findViewById(R.id.newPassword);
        String newPwd = npwd.getText().toString();
        EditText rcpwd = (EditText) findViewById(R.id.reconfirmPassword);
        String reconfirmPwd = rcpwd.getText().toString();
        DBHelper db =new DBHelper(this);
        String sql = "SELECT * FROM users WHERE Username='"+username+"'";
        Cursor rs = db.getData(sql);
        if (rs.moveToFirst()){
            do {
                if (rs.getString(1).equals(currentPwd) && newPwd.equals(reconfirmPwd)) {
                    String sql1 = "UPDATE users SET Pasword= '" + newPwd + "' WHERE Username='" + username + "'";
                    db.executeSQL(sql1);
                    Toast.makeText(this, "Successfully Reset Password", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MyAccount.class);
                    startActivity(intent);
                }else if(currentPwd.equals("")){
                    cpwd.setError("Current Password is required");
                }else if(newPwd.equals("")) {
                    npwd.setError("New Password is required");
                }else if(reconfirmPwd.equals("")){
                    rcpwd.setError("Re-enter password");
                }else if (!newPwd.equals(reconfirmPwd)) {
                    rcpwd.setError("Re-entered password does not match");
                }else {
                    cpwd.setError("Incorrect current password");
                }
            }while(rs.moveToNext());
        }
        rs.close();
    }
}
