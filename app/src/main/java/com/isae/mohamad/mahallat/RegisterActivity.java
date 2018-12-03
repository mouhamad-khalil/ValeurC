package com.isae.mohamad.mahallat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.isae.mohamad.mahallat.Classes.User;
import com.isae.mohamad.mahallat.Classes.utilities.Constants;

import org.json.JSONObject;

import static com.isae.mohamad.mahallat.Classes.utilities.Constants.User_Register_API;

public class RegisterActivity extends AppCompatActivity {

    RequestQueue requestQueue; // This is our requests queue to process our HTTP requests.
    TextView txtEmail, txtFirstName, txtLastName, txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        txtEmail =(TextView)findViewById(R.id.txtEmail);
        txtFirstName =(TextView)findViewById(R.id.txtFirstName);
        txtLastName =(TextView)findViewById(R.id.txtLastName);
        txtUsername =(TextView)findViewById(R.id.txtUsername);
        txtPassword =(TextView)findViewById(R.id.txtPassword);
    }

    public void Register(View v)
    {
        String email = txtEmail.getText().toString();
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        User user = new User("0",username,email,firstName,lastName,password,"");
        JSONObject jsonUser = user.ToJson();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, User_Register_API, jsonUser, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"The user was registered Successfully",Toast.LENGTH_SHORT).show();
                        Intent LoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(LoginIntent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Sorry the user not registered!",Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        requestQueue.add(jsonObjectRequest);
    }
}
