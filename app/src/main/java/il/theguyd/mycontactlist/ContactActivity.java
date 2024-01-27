package il.theguyd.mycontactlist;
import static il.theguyd.mycontactlist.utils.Utils.getWatcherWithValidations;
import android.content.ContentValues;
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




public class ContactActivity extends AppCompatActivity {

    private EditText edtFirstNameContact,edtTelephoneContact,edtEmailContact,edtLastNameContact,edtGenderContact;

    private  Button btnUpdateContact, btnDeleteContact;


    private int contactID;

    private DBHelper databaseHelper;

    private Contact contact ;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable full screen
        EdgeToEdge.enable(this);

        //set the layout for UI
        setContentView(R.layout.activity_contact);
        
        //init DBHelper
        databaseHelper = DBHelper.getInstance(this);
        
        //current contact information is queried 
        contactID = getIntent().getIntExtra("contactID",0);

        //init contact
        contact = getContactInformation();
        Log.d("INFO",contact.toString());

        //init Views
        initEditTextContact();
        initButtonContact();

        //if contact found then load the information to UI
        if(contact !=null){
            //show contact information to the user on the EditTexts
            showContactInformation();

            //set listeners and handlers
            setEditTextContactListenersEventHandlers();
            setButtonContactListenersEventHandlers();
        }


    }

    private Contact getContactInformation() {
        Contact contact = null;
        try{
            contact = databaseHelper.searchContact(contactID);
        }catch (SQLException exception){
            Log.d("ERROR",exception.getMessage());
        }
        return contact;

    }


    private void showContactInformation() {
        edtFirstNameContact.setText(contact.getFirstName());
        edtLastNameContact.setText(contact.getLastName());
        edtTelephoneContact.setText(contact.getTelephone());
        edtEmailContact.setText(contact.getEmail());
        edtGenderContact.setText(contact.getGender());
    }

    private void setEditTextContactListenersEventHandlers() {
        edtFirstNameContact.addTextChangedListener(getWatcherWithValidations(edtFirstNameContact,this));
        edtLastNameContact.addTextChangedListener(getWatcherWithValidations(edtLastNameContact,this));
        edtTelephoneContact.addTextChangedListener(getWatcherWithValidations(edtTelephoneContact,this));
        edtEmailContact.addTextChangedListener(getWatcherWithValidations(edtEmailContact,this));
    }

    private void setButtonContactListenersEventHandlers() {
        btnUpdateContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                long rowID = -1;
                boolean isUpdated = false;

                //this will construct the values to the update query
                ContentValues contentValues = new ContentValues();

                //collect the values from the Edit Texts
                String firstName = edtFirstNameContact.getText().toString();
                String lastName = edtLastNameContact.getText().toString();
                String fullName = firstName + " " + lastName;
                String telephone = edtTelephoneContact.getText().toString();
                String email = edtEmailContact.getText().toString();

                //check which field from EditText was updated
                //the update fields will be sent to to the update query
                boolean isFirstNameChanged = !contact.getFirstName().equals(firstName);
                if (isFirstNameChanged) {
                    contentValues.put("first_name", firstName);
                    contentValues.put("first_name_last_name", fullName);
                }

                boolean isLastNameChanged = !contact.getLastName().equals(lastName);
                if (isLastNameChanged) {
                    contentValues.put("last_name", lastName);
                    contentValues.put("first_name_last_name", fullName);
                }

                boolean isEmailChanged = !contact.getEmail().equals(email);
                if (isEmailChanged) {
                    contentValues.put("email", email);
                }

                boolean isTelephoneChanged = !contact.getTelephone().equals(telephone);
                if (isTelephoneChanged) {
                    contentValues.put("telephone", telephone);
                }

                try {

                    //contentValues.isEmpty() introduced in Android API 30
                    boolean isDiffarences = contentValues.size()>0;
                    if (isDiffarences) {
                        rowID = databaseHelper.updateContact(contactID, contentValues);
                    }
                } catch (Exception exception) {
                    Log.d("ERROR", exception.getMessage());
                    Toast.makeText(ContactActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
                isUpdated = rowID!=-1;
                if (isUpdated) {
                    String message = "contact name" +
                            fullName +
                            " updated";
                    Toast.makeText(ContactActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteContact.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                long rowID = -1;
                boolean isDelete;
                try {
                    rowID = databaseHelper.deleteContact(contactID);

                } catch (Exception exception) {
                    Log.d("ERROR", exception.getMessage());
                    Toast.makeText(ContactActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

                isDelete = rowID!=-1;
                if (isDelete) {
                    String message = "contact name" +
                            contact.getFullName().toString() +
                            " deleted";
                    Toast.makeText(ContactActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }



        });
    }


    private void initButtonContact() {
        btnDeleteContact = findViewById(R.id.btnDeleteContact);
        btnUpdateContact = findViewById(R.id.btnUpdateContact);
    }

    private void initEditTextContact() {
        edtFirstNameContact = findViewById(R.id.edtFirstNameContact);
        edtTelephoneContact = findViewById(R.id.edtTelephoneContact);
        edtEmailContact = findViewById(R.id.edtEmailContact);
        edtLastNameContact = findViewById(R.id.edtLastNameContact);
        edtGenderContact = findViewById(R.id.edtGenderContact);
    }


}