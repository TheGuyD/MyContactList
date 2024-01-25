package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.isValidInput;
import static il.theguyd.mycontactlist.utils.Validations.isValidName;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import il.theguyd.mycontactlist.Models.Contact;
import il.theguyd.mycontactlist.adapter.ContactAdapter;


public class SearchContactActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageView btnSearch;
    private FloatingActionButton btnAddContact;

    private RecyclerView rcContact;

    private ContactAdapter adapter ;

    private int userID;
    private DBHelper databaseHelper;
    ArrayList<Contact> contacts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_contact);
        databaseHelper = new DBHelper(SearchContactActivity.this);

        //get the userID from the previuse activity (SigninActivity) hence it is the forenkey
        //in the contact table
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",0);
        Toast.makeText(this,"userID:"+String.valueOf(userID),Toast.LENGTH_SHORT).show();

        initSearchEditTexts();
        initSearchImageViews();
        initSearchButtonViews();
        initRecyclerViews();



        //initiate adapter to adapt data for RecycleView items
        adapter = new ContactAdapter(this);
        adapter.setContacts(contacts);
        rcContact.setLayoutManager(new LinearLayoutManager(this));
        rcContact.setAdapter(adapter);

        showAllContacts();

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddContactActivity.class);
                i.putExtra("userID", userID);
                startActivity(i);
            }
        });




        edtSearch.addTextChangedListener(new TextWatcher() {
            //TODO: edit the search to except one line only
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showUserContactByName(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showAllContacts() {
        if(isValidInput(edtSearch)){
            //TODO: initialize the search in thread

            //clear all data
            contacts.clear();

            try{

                //add all the contacts of the user after query by userID
                contacts.addAll(databaseHelper.getAllUserContacts(userID));
            }catch (SQLException exception){
                Log.d("ERROR",exception.getMessage().toString());
            }
            if(contacts!= null){

                //notify all observers.
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(SearchContactActivity.this,"no contact found",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showUserContactByName(CharSequence charSequence){

        //clear the contacts so whenever the user input is change only new resaults shown
        contacts.clear();


        if(isValidName(charSequence)){
            //TODO: initialize the search in thread
            try{

                //add all the contacts of the user after query by userID
                contacts.addAll(databaseHelper.searchUserContacts(String.valueOf(charSequence),userID));
            }catch (SQLException exception){
                Log.d("ERROR",exception.getMessage().toString());
            }
            if(contacts!= null){

                //notify all observers.
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(SearchContactActivity.this,"no contact found",Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void initRecyclerViews() {
        rcContact = findViewById(R.id.rcContact);
    }

    private void initSearchButtonViews() {
        btnAddContact = findViewById(R.id.btnAddContact);
    }

    private void initSearchImageViews() {
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void initSearchEditTexts(){
        edtSearch = findViewById(R.id.edtSearch);
    }


}