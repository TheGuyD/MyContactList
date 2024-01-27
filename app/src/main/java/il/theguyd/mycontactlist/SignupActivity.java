package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Utils.getWatcherWithValidations;
import static il.theguyd.mycontactlist.utils.Utils.isValidInput;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {

   private EditText edtFirstName, edtLastName, edtEmailSignup, edtPasswordSignup, edtTelephone;
   private Button btnSignup1;

    private DBHelper databaseHelper;

    private long userID;

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

    private void initButtonSignUp() {
        btnSignup1 = findViewById(R.id.btnSignup1);
    }
    private void initEditTextSignUp() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmailSignup = findViewById(R.id.edtEmailSignup);
        edtPasswordSignup = findViewById(R.id.edtPasswordSignup);
        edtTelephone = findViewById(R.id.edtTelephone);
    }
    private void setEditTextSignUpListenersEventHandlers() {

        //getWatcher implement event handlers, the handlers also call data validations
        edtFirstName.addTextChangedListener(getWatcherWithValidations(edtFirstName, this));
        edtLastName.addTextChangedListener(getWatcherWithValidations(edtLastName, this));
        edtEmailSignup.addTextChangedListener(getWatcherWithValidations(edtEmailSignup, this));
        edtPasswordSignup.addTextChangedListener(getWatcherWithValidations(edtPasswordSignup, this));
        edtTelephone.addTextChangedListener(getWatcherWithValidations(edtTelephone, this));

    }


    private void setButtonsSignupListenersEventHandlers() {
        btnSignup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmailSignup.getText().toString();
                String password = edtPasswordSignup.getText().toString();

                if(isValidInput(edtEmailSignup)){
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
                        Toast.makeText(SignupActivity.this, "email exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignupActivity.this, "enter valid email address", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }




}