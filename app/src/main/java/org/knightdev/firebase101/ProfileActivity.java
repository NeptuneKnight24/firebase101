package org.knightdev.firebase101;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnTouchListener{
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;

    private EditText editTextname,editTextAddress;
    private Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextname = (EditText)findViewById(R.id.et_name);
        editTextAddress = (EditText)findViewById(R.id.et_address);
        buttonSave = (Button)findViewById(R.id.btn_AddPeople);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewUserEmail = (TextView) findViewById(R.id.tv_userEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail());
        buttonLogout = (Button)findViewById(R.id.btn_logout);

        buttonLogout.setOnTouchListener(this);
        buttonSave.setOnTouchListener(this);

    }

    private void saveUserInformation(){
        String name = editTextname.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name,add);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);
        Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show();


    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                if (view.getId() == R.id.btn_logout){
                    firebaseAuth.signOut();
                    startActivity(new Intent(this,LoginActivity.class));
                }else if (view.getId() == R.id.btn_AddPeople){
                    saveUserInformation();
                }
        }
        return false;
    }
}
