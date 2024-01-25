package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.getWatcher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import il.theguyd.mycontactlist.Models.User;
import il.theguyd.mycontactlist.adapter.ContactAdapter;



public class SigninActivity extends AppCompatActivity {
    EditText edtEmailSignin,edtTxtPasswordSignin;
    Button btnSignin1;

    private ContactAdapter adapter;

    DBHelper databaseHelper;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
    databaseHelper = new DBHelper(this);
    initSigninEditText();
    initSigninButton();
    setSigninButtonsListenersAndEventHandlers();
    setSigninEditTextListenersAndEventHandlers();
    }

    private void initSigninButton() {
        btnSignin1 = findViewById(R.id.btnSignin1);
    }

    private void initSigninEditText() {
        edtEmailSignin = findViewById(R.id.edtEmailSignin);
        edtTxtPasswordSignin = findViewById(R.id.edtTxtPasswordSignin);
    }

    public void setSigninEditTextListenersAndEventHandlers(){

        //getWatcher implement Watcher event handler, in addition it also implement data validations
        edtEmailSignin.addTextChangedListener(getWatcher(edtEmailSignin,this));
        edtTxtPasswordSignin.addTextChangedListener(getWatcher(edtTxtPasswordSignin,this));


    }

    public void setSigninButtonsListenersAndEventHandlers(){
        btnSignin1.setOnClickListener(new View.OnClickListener() {
            //event handler
            @Override
            public void onClick(View view) {

                //get the text from use input
                String email = edtEmailSignin.getText().toString();
                String password = edtTxtPasswordSignin.getText().toString();
                try{
                    user = databaseHelper.searchUser(email,password);
                }catch (SQLException exception){
                    Log.d("ERROR",exception.getMessage().toString());
                }
                if(user != null){
                    //start SearchContactActivity
                    Intent i = new Intent(getApplicationContext(), SearchContactActivity.class);
                    i.putExtra("userID", user.getId());
                    startActivity(i);
                }else{
                    Toast.makeText(SigninActivity.this,"wrong email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}