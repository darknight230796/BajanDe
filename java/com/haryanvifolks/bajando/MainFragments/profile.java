package com.haryanvifolks.bajando.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.haryanvifolks.bajando.Activities.PhoneAuthActivity;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.widget.Toast.LENGTH_SHORT;
import static com.haryanvifolks.bajando.MainActivity.mAuth;

public class profile extends Fragment {

    public Button phone;
    public Button logout;
    private static View  v;

    private static String phoneno;

    private static String verificationID;

    private EditText phoneNumber;
    private Button authenticaate;

    private EditText verificationCode;
    private Button verifyCode;

    // WebView webView;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {



        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            v = inflater.inflate(R.layout.profile_fragment_1, container, false);
            logout = v.findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                   Toast.makeText(MainActivity.getMainContext(),"Logged out",LENGTH_SHORT).show();
                }
            });
        }
        else {
            v = inflater.inflate(R.layout.profile_fragment_2, container, false);
            phone = v.findViewById(R.id.new_user);
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.getMainContext(), PhoneAuthActivity.class);
                    startActivity(intent);

                }
            });

        }
        return v;
    }


}
