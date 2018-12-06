package com.isae.mohamad.mahallat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.isae.mohamad.mahallat.Classes.User;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;

public class ProfileActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        try {
            user = MyApplication.GetUserInfo();
            if (user != null) {
                ((TextView) findViewById(R.id.txtUsername)).setText(user.getUsername());
                ((TextView) findViewById(R.id.txtFirstname)).setText(user.getName());
                ((TextView) findViewById(R.id.txtLastname)).setText(user.getLastname());
                ((TextView) findViewById(R.id.txtEmail)).setText(user.getEmail());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK, null);
        onBackPressed();
        return true;
    }
}
