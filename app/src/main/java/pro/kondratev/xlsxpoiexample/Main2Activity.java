package pro.kondratev.xlsxpoiexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends Activity implements View.OnClickListener {

    Button b1;
    TextView email,pass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        b1=(Button)findViewById(R.id.button2);
        email=(TextView)findViewById(R.id.email);
        pass=(TextView)findViewById(R.id.pass);
        b1.setOnClickListener(Main2Activity.this);

    }

    @Override
    public void onClick(View v) {
        String email1 = email.getText().toString();
        final String password = pass.getText().toString();
        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email1,password).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    // there was an error
                    Toast.makeText(Main2Activity.this, "Please check your username or password", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Main2Activity.this, ReadExcel.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }



    }
