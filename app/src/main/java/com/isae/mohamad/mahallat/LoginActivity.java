package com.isae.mohamad.mahallat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public  void SignInClick(View v){
        Intent ProductsIntent = new Intent(this, ProductsActivity.class);
        startActivity(ProductsIntent);
    }
    
    public void RegisterClick(View v) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void ForgetPasswordClick(View v) {
        Intent ForgetIntent = new Intent(this, ForgetPassActivity.class);
        startActivity(ForgetIntent);
    }
}
