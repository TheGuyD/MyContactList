package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Utils.isValidInput;
import static il.theguyd.mycontactlist.utils.Utils.isValidName;

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
    ArrayList<Contact> Contacts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable full screen
        EdgeToEdge.enable(this);

        //set the layout for UI
        setContentView(R.layout.activity_search_contact);

        //init DBHelper
        databaseHelper = DBHelper.getInstance(SearchContactActivity.this);

        //get the userID from the  previous activity (SigninActivity) hence it is the foreign key
        //in the contact table
        userID = getIntent().getIntExtra("userID",0);
        Toast.makeText(this,"userID:"+userID,Toast.LENGTH_SHORT).show();

        //init Views
        initEditTextsSearch();
        initImageViewsSearch();
        initButtonsSearch();
        initRecyclerViews();



        //init adapter to adapt data for RecycleView items
        adapter = new ContactAdapter(this);
        adapter.setContacts(Contacts);
        rcContact.setLayoutManager(new LinearLayoutManager(this));
        rcContact.setAdapter(adapter);

        //when SearchContactActivity is load it show all contacts
        //that belongs to useID
        showAllContacts();


        //set listeners and handlers
        setFloatingActionButtonSearchContactsListenersAndEventsHandlers();
        setEditTextsSearchContactsListenersAndEventHandlers();


    }

    private void setEditTextsSearchContactsListenersAndEventHandlers() {
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

    private void setFloatingActionButtonSearchContactsListenersAndEventsHandlers() {
        btnAddContact.setOnClickListener(new View.OnClickListener() {

           //user clicked on the floating plus button
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);

                //pass to AddContactActivity
                intent.putExtra("userID", userID);

                //start AddContactActivity
                startActivity(intent);
            }
        });
    }

    private void showAllContacts() {
       boolean isFound = false;
        if(isValidInput(edtSearch)){

            //clear all data
            Contacts.clear();

            try{

                //add all the contacts of the user after query by userID
                Contacts.addAll(databaseHelper.searchAllUserContacts(userID));
            }catch (Exception exception){
                Log.d("ERROR",exception.getMessage());
            }
            isFound = Contacts != null;
            if(isFound){

                //notify all observers.
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(SearchContactActivity.this,"no contact found",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showUserContactByName(CharSequence charSequence){

        //clear the contacts so whenever the user input is change only new results shown
        Contacts.clear();


        if(isValidName(charSequence)){
            //TODO: initialize the search in thread
            try{

                //add all the contacts of the user after query by userID
                Contacts.addAll(databaseHelper.searchUserContacts(String.valueOf(charSequence),userID));
            }catch (SQLException exception){
                Log.d("ERROR",exception.getMessage().toString());
            }
            if(Contacts != null){

                //notify all observers.
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(SearchContactActivity.this,"no contact found",Toast.LENGTH_SHORT).show();
            }

        }

    }



    //whenever user return to the current activity, right before it fully loaded
    //the contacts are refresh
    @Override
    protected void onResume() {
        super.onResume();
        showAllContacts();
    }


    private void initRecyclerViews() {
        rcContact = findViewById(R.id.rcContact);
    }

    private void initButtonsSearch() {
        btnAddContact = findViewById(R.id.btnAddContact);
    }

    private void initImageViewsSearch() {
        btnSearch = findViewById(R.id.btnSearch);
    }

    private void initEditTextsSearch(){
        edtSearch = findViewById(R.id.edtSearch);
    }

    public  ArrayList<Contact> getContacts(){
        return this.Contacts;
    }



}