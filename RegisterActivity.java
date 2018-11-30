package com.isae.mohamad.mahallat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.isae.mohamad.mahallat.Classes.User;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;


public class RegisterActivity extends AppCompatActivity {

    TextView txtEmail, txtFirstName, txtLastName, txtUsername, txtPassword;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail =(TextView)findViewById(R.id.txtEmail);
        txtFirstName =(TextView)findViewById(R.id.txtFirstName);
        txtLastName =(TextView)findViewById(R.id.txtLastName);
        txtUsername =(TextView)findViewById(R.id.txtUsername);
        txtPassword =(TextView)findViewById(R.id.txtPassword);

        /*Create handle for the RetrofitInstance interface*/
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register(view);
            }
        });
    }

    public void Register(View v)
    {
        String email = txtEmail.getText().toString();
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        User user = new User(0,username,email,firstName,lastName,password,"");

        /*Call the method in the interface to register a user*/
        Call<JSONObject> call  = apiInterface.doUserRegister( user);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                if (response.isSuccessful() ) {
                    Toast.makeText(getApplicationContext(), "You have registered, thank you!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sorry, there is a problem!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
