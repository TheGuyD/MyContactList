package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Utils.getWatcherWithValidations;
import static il.theguyd.mycontactlist.utils.Utils.isValidInput;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddContactActivity extends AppCompatActivity {

    private EditText edtFirstNameAddContact,edtLastNameAddContact,edtTelephoneAddContact,edtEmailAddContact;
    private Button btnAddContactAddContact;

    private DBHelper databaseHelper;
    private int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable full screen
        EdgeToEdge.enable(this);

        //set the layout for UI
        setContentView(R.layout.activity_add_contact);

        //init DBHelper
        databaseHelper = DBHelper.getInstance(this);

        //get userID from SearchActivity
        userID = getIntent().getIntExtra("userID",0);

        //init Views
        initEditTextAddContact();
        initButtonsAddContact();

        //set listeners and handlers
        setEditTextAddContactEListenersEventHandlers();
        setButtonsAddContactListenersHandlers();



    }

    private void initButtonsAddContact() {
        btnAddContactAddContact = findViewById(R.id.btnAddContactAddContact);
    }

    private void initEditTextAddContact() {
        edtFirstNameAddContact = findViewById(R.id.edtFirstNameAddContact);
        edtLastNameAddContact = findViewById(R.id.edtLastNameAddContact);
        edtTelephoneAddContact = findViewById(R.id.edtTelephoneAddContact);
        edtEmailAddContact = findViewById(R.id.edtEmailAddContact);
    }

    private void setEditTextAddContactEListenersEventHandlers() {
        edtFirstNameAddContact.addTextChangedListener(getWatcherWithValidations(edtFirstNameAddContact,this));
        edtLastNameAddContact.addTextChangedListener(getWatcherWithValidations(edtLastNameAddContact,this));
        edtTelephoneAddContact.addTextChangedListener(getWatcherWithValidations(edtTelephoneAddContact,this));
        edtEmailAddContact.addTextChangedListener(getWatcherWithValidations(edtEmailAddContact,this));
    }

    private void setButtonsAddContactListenersHandlers() {

        btnAddContactAddContact.setOnClickListener(new View.OnClickListener() {

            //user clicked on the plus sign button
            @Override
            public void onClick(View view) {


                //extract info from EditTexts
                String firstName =  edtFirstNameAddContact.getText().toString();
                String lastName = edtLastNameAddContact.getText().toString();
                String email = edtEmailAddContact.getText().toString();
                String telephone = edtTelephoneAddContact.getText().toString();
                String fullName = firstName+" "+lastName;


                if(isValidInput(edtEmailAddContact)&&isValidInput(edtTelephoneAddContact)){
                    fetchGenderDataAndInsertToDB(firstName,lastName,fullName,email,telephone);
                }else{
                    Toast.makeText(AddContactActivity.this,"enter valid email or phone number",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }



    private void fetchGenderDataAndInsertToDB(String firstName,String lastName,String fullName, String email,String telephone ){

        //prerequisites for api calls to fetch gender
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.genderize.io").addConverterFactory(GsonConverterFactory.create()).build();
        GenderizeService genderizeService = retrofit.create(GenderizeService.class);
        Call<Genderize> call = genderizeService.getGenderize(edtFirstNameAddContact.getText().toString());

        //Android not allowed to make INTERNET calls from the main thread hence we use thread
        call.enqueue(new Callback<Genderize>() {
            @Override
            public void onResponse(Call<Genderize> call, Response<Genderize> response) {
                if(response.isSuccessful()){
                    boolean isInsert=false;
                    long rowID = -1;

                    //gender fetched using Retrofit
                    String gender = response.body().getGender();

                    try {

                        //insert data to db
                        rowID = databaseHelper.insertContact(String.valueOf(userID),firstName,lastName,fullName,email,telephone,gender);

                    }catch (Exception exception){
                        Toast.makeText(AddContactActivity.this,exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    isInsert = rowID!=-1;
                    if(isInsert){

                        Toast.makeText(AddContactActivity.this,"contact added to your contact list",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddContactActivity.this,"telephone is UNIQUE",Toast.LENGTH_SHORT).show();
                    }

                    }else{
                            if(response.code() == 429){
                                Toast.makeText(AddContactActivity.this,"genderize API rate limit",Toast.LENGTH_SHORT).show();

                            }


                }

            }

            @Override
            public void onFailure(Call<Genderize> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


}