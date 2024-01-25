package il.theguyd.mycontactlist;

import static il.theguyd.mycontactlist.utils.Validations.isValidInput;
import static il.theguyd.mycontactlist.utils.Validations.isValidName;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
                searchContactByName(charSequence);
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

    private void searchContactByName(CharSequence charSequence){

        //clear the contacts so whenever the user input is change only new resaults shown
        contacts.clear();
        adapter.notifyDataSetChanged();
        if(isValidName(charSequence)){
            //TODO: initialize the search in thread
            try{
                DBHelper databaseHelper = new DBHelper(SearchContactActivity.this);
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                String firstNameLastName = "%"+charSequence.toString()+"%";
                String[] s = new String[]{String.valueOf(userID),firstNameLastName};
                Cursor cursor = db.rawQuery("SELECT first_name,last_name,email,telephone FROM contact WHERE user_id=? AND first_name_last_name LIKE ? ",s);
                if(cursor!=null){
                    if(cursor.moveToFirst()){
                        for (int i=0;i<cursor.getCount();i++){
                            String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                            String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                            String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                            String email = cursor.getString(cursor.getColumnIndex("email"));
                            contacts.add(new Contact(firstName,lastName,firstName+ " " +lastName,email,telephone));

                            //move to the next row in the database
                            cursor.moveToNext();
                        }
                        adapter.notifyDataSetChanged();
                        cursor.close();
                        db.close();
                    }else{
                        Toast.makeText(SearchContactActivity.this,"contact not found",Toast.LENGTH_SHORT).show();

                        //close database connection
                        cursor.close();
                        db.close();
                    }
                }else{
                    //cursor is null so only close db connection
                    db.close();
                }

        }catch (SQLException exception){
                Toast.makeText(SearchContactActivity.this,exception.getMessage(),Toast.LENGTH_LONG).show();
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