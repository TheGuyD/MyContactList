package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.getWatcher;





import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {

    EditText edtFirstName, edtLastName, edtEmailSignup, edtPasswordSignup, edtTelephone;
    Button btnSignup1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


        initSignUpButtons();
        setSignupButtonsListenersAndEventHandlers();
        initSignUpEditText();
        setSignupEditTextListenersAndEventHandlers();

    }

    public void initSignUpButtons() {
        btnSignup1 = findViewById(R.id.btnSignup1);
    }

    public void setSignupButtonsListenersAndEventHandlers() {
        btnSignup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : implement insertion to the db of a user
            }
        });
    }

    public void initSignUpEditText() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmailSignup = findViewById(R.id.edtEmailSignup);
        edtPasswordSignup = findViewById(R.id.edtPasswordSignup);
        edtTelephone = findViewById(R.id.edtTelephone);
    }

    public void setSignupEditTextListenersAndEventHandlers() {

        //getWatcher implement event handlers, the handlers also call data validations
        edtFirstName.addTextChangedListener(getWatcher(edtFirstName, this));
        edtLastName.addTextChangedListener(getWatcher(edtLastName, this));
        edtEmailSignup.addTextChangedListener(getWatcher(edtEmailSignup, this));
        edtPasswordSignup.addTextChangedListener(getWatcher(edtPasswordSignup, this));
        edtTelephone.addTextChangedListener(getWatcher(edtTelephone, this));

    }


}