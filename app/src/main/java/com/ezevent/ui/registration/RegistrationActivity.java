package com.ezevent.ui.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ezevent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    String mVerificationId;

    TextInputLayout textInputLayoutEmail,textInputLayoutMobile,textInputLayoutUserName,textInputLayoutPassword,textInputLayoutCnfPassword;
    TextInputEditText textInputEditTextEmail,textInputEditTextMobile,textInputEditTextPassword,textInputEditTextUserName,textInputEditTextCnfPassowrd;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textInputLayoutEmail=findViewById(R.id.textInputLayoutUserEmail);
        textInputLayoutMobile=findViewById(R.id.textInputLayoutMobileNumber);
        textInputLayoutUserName=findViewById(R.id.textInputLayoutUserName);
        textInputLayoutPassword=findViewById(R.id.textInputLayoutPassword);
        textInputLayoutCnfPassword=findViewById(R.id.textInputLayoutCnfPassword);

        textInputEditTextEmail=findViewById(R.id.userEmail);
        textInputEditTextMobile=findViewById(R.id.userMobileNumber);
        textInputEditTextUserName=findViewById(R.id.userName);
        textInputEditTextPassword=findViewById(R.id.userPassword);
        textInputEditTextCnfPassowrd=findViewById(R.id.userCnfPassword);

        mAuth = FirebaseAuth.getInstance();
//        PhoneAuthProvider.getInstance().verifyPhoneNumber("+917058239556", 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                Toast.makeText(RegistrationActivity.this, "Verification Completed ", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//
//                Log.e("Verification Exception ", e.getLocalizedMessage());
//                e.printStackTrace();
//                Toast.makeText(RegistrationActivity.this, "Verification Failed  ", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
//                super.onCodeSent(verificationId, token);
//
//                Log.d("Phone Authentication ", "onCodeSent:" + verificationId);
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                openDialog();
//            }
//
//            @Override
//            public void onCodeAutoRetrievalTimeOut(String s) {
//                super.onCodeAutoRetrievalTimeOut(s);
//            }
//        });
    }

    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View view = inflater.inflate(R.layout.dialog_otp, null, false);

        final EditText editTextOTP = view.findViewById(R.id.edittextOTP);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if (editTextOTP.getText().toString().isEmpty() && editTextOTP.getText().toString().length() < 6) {
                            Toast.makeText(RegistrationActivity.this, "Enter Correct OTP ", Toast.LENGTH_SHORT).show();
                        } else {
                            verifyWithCode(editTextOTP.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();


    }

    void verifyWithCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
//                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
//                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    public void createNewUser(View view) {
        boolean isAllSet=true;
        if (textInputEditTextEmail.getText().toString().isEmpty())
        {
            textInputLayoutEmail.setError("Enter Email ID ");
            isAllSet=false;
        }

        if (textInputEditTextUserName.getText().toString().isEmpty())
        {
            textInputLayoutUserName.setError("Enter User Name ");
            isAllSet=false;
        }
        if (textInputEditTextMobile.getText().toString().isEmpty())
        {
            textInputLayoutEmail.setError("Enter Mobile Number");
            isAllSet=false;
        }
        if (textInputEditTextPassword.getText().toString().isEmpty())
        {
            textInputLayoutPassword.setError("Enter Password");
            isAllSet=false;
        }
        if (textInputEditTextCnfPassowrd.getText().toString().isEmpty())
        {
            textInputLayoutCnfPassword.setError("Enter Email ID ");
            isAllSet=false;
        }
        if (!textInputEditTextCnfPassowrd.getText().toString().trim().equals(textInputEditTextPassword.getText().toString().trim()))
        {
            textInputLayoutCnfPassword.setError("Password Not Matched ");
            isAllSet=false;
        }

        if (textInputEditTextMobile.getText().toString().trim().length()!=10)
        {
            textInputLayoutMobile.setError("Invalid Mobile Number");
            isAllSet=false;
        }
        if (isAllSet)
        {
            Toast.makeText(this, "All data filled ", Toast.LENGTH_SHORT).show();
        }

    }
}
