package com.haryanvifolks.bajando.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_SHORT;
import static com.haryanvifolks.bajando.MainActivity.mAuth;

public class PhoneAuthActivity extends AppCompatActivity {
    public Button phone;
    public Button logout;

    private static String phoneno;

    private static String verificationID;

    private EditText phoneNumber;
    private Button authenticaate;

    private EditText verificationCode;
    private Button verifyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment_3);

        mAuth = FirebaseAuth.getInstance();

        phoneNumber = findViewById(R.id.username);
        authenticaate = findViewById(R.id.login_button);

        verificationCode = findViewById(R.id.verficationcode);
        verifyCode = findViewById(R.id.verifycode);


        authenticaate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno = "+91"+phoneNumber.getText().toString();
                if (phoneno.length()!=13) {
                    Toast.makeText(MainActivity.getMainContext(), "Enter a valid phone number", LENGTH_SHORT).show();
                }
                else{
                    codeSend(phoneno);
                }
            }
        });


        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code1 = verificationCode.getText().toString();
                verifyCodeManually(code1);
            }
        });

    }

    private void verifyCodeManually(String code){
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.getMainContext(), "Successful", LENGTH_SHORT).show();
                                Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.getMainContext(), "Failed to verify code", LENGTH_SHORT).show();
                            }
                        }
                    });
        }catch (Exception e){
            Log.e("FIREBASE",e.getMessage());
        }
    }

    private void codeSend(String phoneno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneno,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationID = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code!=null){
                        verifyCodeManually(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(MainActivity.getMainContext(), "Error in verification", LENGTH_SHORT).show();

                    Log.e("FIREBASE",e.getMessage());
                }
            };

}
