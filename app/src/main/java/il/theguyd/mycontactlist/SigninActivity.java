package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Utils.getWatcherWithValidations;
import static il.theguyd.mycontactlist.utils.Utils.isValidInput;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import il.theguyd.mycontactlist.Models.User;



public class SigninActivity extends AppCompatActivity {
    private EditText edtEmailSignin,edtTxtPasswordSignin;
    private Button btnSignin1;



    private DBHelper databaseHelper;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable full screen
        EdgeToEdge.enable(this);

        //set the layout for UI
        setContentView(R.layout.activity_signin);

        //init DBHelper instance
        databaseHelper = DBHelper.getInstance(this);

        //init Views
        initEditTextSignin();
        initButtonsSignin();

        //set listeners and handlers
        setButtonsSigninListenersEventHandlers();
        setEditTextSigninListenersEventHandlers();
    }

    private void initButtonsSignin() {
        btnSignin1 = findViewById(R.id.btnSignin1);
    }

    private void initEditTextSignin() {
        edtEmailSignin = findViewById(R.id.edtEmailSignin);
        edtTxtPasswordSignin = findViewById(R.id.edtTxtPasswordSignin);
    }

    private void setEditTextSigninListenersEventHandlers(){

        //getWatcher implement Watcher event handler, in addition it also implement data validations
        edtEmailSignin.addTextChangedListener(getWatcherWithValidations(edtEmailSignin,this));
        edtTxtPasswordSignin.addTextChangedListener(getWatcherWithValidations(edtTxtPasswordSignin,this));


    }

    private void setButtonsSigninListenersEventHandlers(){
        btnSignin1.setOnClickListener(new View.OnClickListener() {

            //user clicked signin
            @Override
            public void onClick(View view) {

                //get email and password from user input
                String email = edtEmailSignin.getText().toString();
                String password = edtTxtPasswordSignin.getText().toString();
                if(isValidInput(edtEmailSignin)){
                    try{

                        //validate user email and password using the database;
                        user = databaseHelper.searchUser(email,password);
                    }catch (SQLException exception){
                        Log.d("ERROR",exception.getMessage().toString());
                    }

                    if(user != null){


                        Intent intent = new Intent(getApplicationContext(), SearchContactActivity.class);

                        //pass userID to SearchContactActivity
                        intent.putExtra("userID", user.getId());

                        //start SearchContactActivity
                        startActivity(intent);
                    }else{
                        Toast.makeText(SigninActivity.this,"wrong email or password",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SigninActivity.this,"enter valid email",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }



}