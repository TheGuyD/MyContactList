package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.getWatcher;

import android.content.ContentValues;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import il.theguyd.mycontactlist.Models.Contact;

public class ContactActivity extends AppCompatActivity {
    //TODO: what about edtGender?
    //TODO: email can update even when is wrong
    EditText edtFirstNameContact,edtTelephoneContact,edtEmailContact,edtLastNameContact;

    Button btnUpdateContact, btnDeleteContact;

    Contact contact;
    int contactID;

    DBHelper databaseHelper;

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
    }

    private void setEditTextContactListenersEventHandlers() {
        edtFirstNameContact.addTextChangedListener(getWatcher(edtFirstNameContact,this));
        edtLastNameContact.addTextChangedListener(getWatcher(edtLastNameContact,this));
        edtTelephoneContact.addTextChangedListener(getWatcher(edtTelephoneContact,this));
        edtEmailContact.addTextChangedListener(getWatcher(edtEmailContact,this));
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
                    contact.setFirstName(firstName);
                    contact.setFullName(fullName);
                    contentValues.put("first_name", firstName);
                    contentValues.put("first_name_last_name", fullName);
                }
                boolean isLastNameChanged = !contact.getLastName().equals(lastName);
                if (isLastNameChanged) {
                    contact.setLastName(lastName);
                    contact.setFullName(fullName);
                    contentValues.put("last_name", lastName);
                    contentValues.put("first_name_last_name", fullName);
                }
                boolean isEmailChanged = !contact.getEmail().equals(email);
                if (isEmailChanged) {
                    contact.setEmail(email);
                    contentValues.put("email", email);
                }
                boolean isTelephoneChanged = !contact.getTelephone().equals(telephone);
                if (isTelephoneChanged) {
                    contact.setTelephone(telephone);
                    contentValues.put("telephone", telephone);
                }

                try {

                    //contentValues.isEmpty() introduced in Android API 30
                    boolean isDiffarences = contentValues.size() != 0;
                    if (isDiffarences) {
                        rowID = databaseHelper.updateContact(contactID, contentValues);
                    }
                } catch (Exception exception) {
                    Log.d("ERROR", exception.getMessage().toString());
                    Toast.makeText(ContactActivity.this, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                isUpdated = rowID!=-1;
                if (isUpdated) {
                    StringBuilder message = new StringBuilder();
                    message.append("contact name");
                    message.append(fullName);
                    message.append(" updated");
                    Toast.makeText(ContactActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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
                    Log.d("ERROR", exception.getMessage().toString());
                    Toast.makeText(ContactActivity.this, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                isDelete = rowID!=-1;
                if (isDelete) {
                    StringBuilder message = new StringBuilder();
                    message.append("contact name");
                    message.append(contact.getFullName().toString());
                    message.append(" deleted");
                    Toast.makeText(ContactActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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
    }


}