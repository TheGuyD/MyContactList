package il.theguyd.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


/*

   naming convention for Views init and set Views:
   init ViewType ActivityName extras

 */
public class MainActivity extends AppCompatActivity {

    Button btnSignin0, btnSignup0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable full screen
        EdgeToEdge.enable(this);

        //set the layout for UI
        setContentView(R.layout.activity_main);

        //init Views
        initButtonsMain();

        //set listeners and handlers
        setButtonsMainListenersEventHandlers();
        }
    public void initButtonsMain(){
        btnSignin0 = findViewById(R.id.btnSignin0);
        btnSignup0 = findViewById(R.id.btnSignup0);
    }

    public void setButtonsMainListenersEventHandlers() {
        btnSignin0.setOnClickListener(new View.OnClickListener() {

            //user clicked signin
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SigninActivity.class);

                //start SigninActivity
                startActivity(intent);
            }
        });
        btnSignup0.setOnClickListener(new View.OnClickListener() {

            //user clicked signup
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);

                //start SignupActivity
                startActivity(intent);
            }
        });
    }

}