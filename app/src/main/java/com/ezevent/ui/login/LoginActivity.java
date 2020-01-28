package com.ezevent.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ezevent.R;
import com.ezevent.ui.forgot.ForgotActivity;
import com.ezevent.ui.registration.RegistrationActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mobileNumber,password;
    TextInputLayout textInputLayoutMobileNumber,textInputLayoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobileNumber=findViewById(R.id.userMobileNumber);
        password=findViewById(R.id.userPassword);
        textInputLayoutMobileNumber=findViewById(R.id.textInputLayoutMobileNumber);
        textInputLayoutPassword=findViewById(R.id.textInputLayout2);
    }

    public void createNewUser(View view) {

        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotActivity.class));
    }

    public void login(View view) {
        boolean allFieldSet=true;
        if (mobileNumber.getText().toString().isEmpty())
        {
            allFieldSet=false;
        }
    }
}
