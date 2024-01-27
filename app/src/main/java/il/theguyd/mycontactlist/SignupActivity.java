package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.getWatcher;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import il.theguyd.mycontactlist.Models.User;


public class SignupActivity extends AppCompatActivity {

    EditText edtFirstName, edtLastName, edtEmailSignup, edtPasswordSignup, edtTelephone;
    Button btnSignup1;

    DBHelper databaseHelper;
    User user;

    long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable full screen
        EdgeToEdge.enable(this);

        //set the layout for UI
        setContentView(R.layout.activity_signup);

        //init DBHelper
        databaseHelper = DBHelper.getInstance(this);

        //init Views
        initButtonSignUp();
        initEditTextSignUp();

        //set listeners and handlers
        setButtonsSignupListenersEventHandlers();
        setEditTextSignUpListenersEventHandlers();

    }

    public void initButtonSignUp() {
        btnSignup1 = findViewById(R.id.btnSignup1);
    }

    public void setButtonsSignupListenersEventHandlers() {
        btnSignup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmailSignup.getText().toString();
                String password = edtPasswordSignup.getText().toString();

                try{
                    userID = databaseHelper.insertUser(email,password);
                }catch (Exception exception){
                    Toast.makeText(SignupActivity.this, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("ERROR",exception.getMessage());
                }

                if(userID!=-1){
                    Intent intent = new Intent(getApplicationContext(), SearchContactActivity.class);
                    intent.putExtra("userID", (int)userID);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SignupActivity.this, "email exist or wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void initEditTextSignUp() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmailSignup = findViewById(R.id.edtEmailSignup);
        edtPasswordSignup = findViewById(R.id.edtPasswordSignup);
        edtTelephone = findViewById(R.id.edtTelephone);
    }

    public void setEditTextSignUpListenersEventHandlers() {

        //getWatcher implement event handlers, the handlers also call data validations
        edtFirstName.addTextChangedListener(getWatcher(edtFirstName, this));
        edtLastName.addTextChangedListener(getWatcher(edtLastName, this));
        edtEmailSignup.addTextChangedListener(getWatcher(edtEmailSignup, this));
        edtPasswordSignup.addTextChangedListener(getWatcher(edtPasswordSignup, this));
        edtTelephone.addTextChangedListener(getWatcher(edtTelephone, this));

    }


}