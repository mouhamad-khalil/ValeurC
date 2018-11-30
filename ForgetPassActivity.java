package com.isae.mohamad.mahallat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.Constants;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassActivity extends AppCompatActivity {

    APIInterface apiInterface;
    EditText txtEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        txtEmail = (EditText) findViewById(R.id.txtEmail);

        /*Create handle for the RetrofitInstance interface*/
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    // Called from XML layout
    public  void ResetClick(View v){
        String email = txtEmail.getText().toString();

        Call<JSONObject> call  = apiInterface.doForgetPassword( email);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                if (response.isSuccessful() ) {
                    Toast.makeText(ForgetPassActivity.this, "An email has been sent to you!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(ForgetPassActivity.this, "Sorry, there is a problem!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
