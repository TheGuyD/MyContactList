package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.getWatcher;

import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import il.theguyd.mycontactlist.Models.Contact;

public class AddContactActivity extends AppCompatActivity {

    EditText edtFirstNameAddContact,edtLastNameAddContact,edtTelephoneAddContact,edtEmailAddContact;
    Button btnAddContactAddContact;

    DBHelper databaseHelper;
    int userID;

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

    private void setButtonsAddContactListenersHandlers() {

        btnAddContactAddContact.setOnClickListener(new View.OnClickListener() {

            //user clicked on the plus sign button
            @Override
            public void onClick(View view) {

                //extract info from EditTexts
                Contact contact = new Contact(edtFirstNameAddContact.getText().toString(),edtLastNameAddContact.getText().toString(),edtEmailAddContact.getText().toString(),edtTelephoneAddContact.getText().toString());

                boolean isInsert=false;
                long rowID = -1;

                try {

                    //insert contact
                    rowID = databaseHelper.insertContact(String.valueOf(userID),contact.getFirstName(), contact.getLastName(), contact.getFullName(), contact.getTelephone(), contact.getEmail());

                }catch (Exception exception){
                    Log.d("ERROR",exception.getMessage().toString());
                }

                isInsert = rowID!=-1;
                if(isInsert){

                    Toast.makeText(AddContactActivity.this,"contact added to your contact list",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddContactActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void setEditTextAddContactEListenersEventHandlers() {
        edtFirstNameAddContact.addTextChangedListener(getWatcher(edtFirstNameAddContact,this));
        edtLastNameAddContact.addTextChangedListener(getWatcher(edtLastNameAddContact,this));
        edtTelephoneAddContact.addTextChangedListener(getWatcher(edtTelephoneAddContact,this));
        edtEmailAddContact.addTextChangedListener(getWatcher(edtEmailAddContact,this));
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


}