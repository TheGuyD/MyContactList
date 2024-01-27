package il.theguyd.mycontactlist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import il.theguyd.mycontactlist.Models.Contact;
import il.theguyd.mycontactlist.Models.User;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database5";
    private static final int DB_VERSION = 1;

    private static DBHelper instance;
    User user = null;



    private DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //singleton to make sure only one instance of the database is used in the entire application
    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table named user with column (id,first_name,last_name,email,password,telephone)
        String sqlStatementCreateUserTable = "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT , first_name TEXT  , last_name TEXT , email TEXT NOT NULL UNIQUE , telephone TEXT , password TEXT NOT NULL )";

        //create table named contact with column (id,user_id,first_name,last_name,first_name_last_name,gender,email,telephone)
        String sqlStatementCreateContactTable = "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT , user_id  INTEGER , first_name TEXT NOT NULL , last_name TEXT NOT NULL , first_name_last_name  TEXT , gender TEXT , email TEXT , telephone TEXT  NOT NULL UNIQUE, FOREIGN KEY (user_id) REFERENCES user(id) )";

        //execute creation
        db.execSQL(sqlStatementCreateUserTable);
        db.execSQL(sqlStatementCreateContactTable);

        initDatabase(db);
    }

    private void initDatabase(SQLiteDatabase db) {
        ContentValues firstUser = new ContentValues();

        firstUser.put("first_name", "Guy");
        firstUser.put("last_name", "David");
        firstUser.put("telephone", "0525517418");
        firstUser.put("email", "gaidavid1@gmail.com");
        firstUser.put("password", "123456");

        long userID1 = db.insert("user", null, firstUser);

        ContentValues secondUser = new ContentValues();

        secondUser.put("first_name", "Danny");
        secondUser.put("last_name", "Danon");
        secondUser.put("telephone", "0582224653");
        secondUser.put("email", "DannyDa@gmail.com");
        secondUser.put("password", "123456");

        long userID2 = db.insert("user", null, secondUser);


        ContentValues firstContact = new ContentValues();


        firstContact.put("user_id", userID1);
        firstContact.put("first_name", "meisam");
        firstContact.put("last_name", "abdul");
        firstContact.put("first_name_last_name", "meisam abdul");
        firstContact.put("gender", "male");
        firstContact.put("telephone", "097433225");
        firstContact.put("email", "meisam@arduan.com");


        db.insert("contact", null, firstContact);

        ContentValues secondContact = new ContentValues();


        secondContact.put("user_id", userID1);
        secondContact.put("first_name", "mubarak");
        secondContact.put("last_name", "husein");
        secondContact.put("first_name_last_name", "mubarak husein");
        secondContact.put("gender", "male");
        secondContact.put("telephone", "06666666");
        secondContact.put("email", "alla@wakbar.com");


        db.insert("contact", null, secondContact);

        ContentValues thirdContact = new ContentValues();


        thirdContact.put("user_id", userID2);
        thirdContact.put("first_name", "bamba");
        thirdContact.put("last_name", "nugat");
        thirdContact.put("first_name_last_name", "bamba nugat");
        thirdContact.put("gender", "male");
        thirdContact.put("telephone", "055323232");
        thirdContact.put("email", "meisam@meisam.com");


        db.insert("contact", null, thirdContact);

        ContentValues fourthContact = new ContentValues();


        fourthContact.put("user_id", userID2);
        fourthContact.put("first_name", "muhammad");
        fourthContact.put("last_name", "deff");
        fourthContact.put("first_name_last_name", "muhammad deff");
        fourthContact.put("gender", "male");
        fourthContact.put("telephone", "6666666");
        fourthContact.put("email", "alla@hammas.com");


        db.insert("contact", null, fourthContact);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //searchUser, query for userID based on email and password
    @SuppressLint("Range")
    public User searchUser(String email, String password) {

        //init values for query place holders to prevent SQL injection attacks
        String[] s = new String[]{email, password};

        //query on readable database using the email and password instead the place holders to avoid SQL injections
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT id,first_name,email,telephone FROM user WHERE email=?AND password=?", s);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                //found user
                user = new User(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("first_name")), cursor.getString(cursor.getColumnIndex("email")), cursor.getString(cursor.getColumnIndex("telephone")));
                Log.d("INFO", user.toString());

                //close instance
                cursor.close();
            }
        }

        return user;
    }

    //get all the contactss of specified user
    @SuppressLint("Range")
    public ArrayList<Contact> searchAllUserContacts(int userID) {
        ArrayList<Contact> Contacts = new ArrayList<>();
        String[] s = new String[]{String.valueOf(userID)};
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM contact WHERE user_id=?", s);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                    String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                    String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String gender = cursor.getString(cursor.getColumnIndex("gender"));
                    int id = cursor.getInt( cursor.getColumnIndex("id") );

                    Contacts.add(new Contact(firstName, lastName, email, telephone, gender, id));

                    //move to the next row in the database
                    cursor.moveToNext();
                }
                cursor.close();
            }

        }
        return Contacts;
    }

    //search contacts of specified user
    @SuppressLint("Range")
    public ArrayList<Contact> searchUserContacts(String name, int userID) {
        ArrayList<Contact> Contacts = new ArrayList<Contact>();
        String firstNameLastName = "%" + name + "%";
        String[] s = new String[]{String.valueOf(userID), firstNameLastName};
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM contact WHERE user_id=? AND first_name_last_name LIKE ? ", s);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                    String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                    String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    int id = cursor.getInt( cursor.getColumnIndex("id") );
                    String gender = cursor.getString(cursor.getColumnIndex("gender"));

                    Contacts.add(new Contact(firstName, lastName, email, telephone,gender , id));

                    //move to the next row in the database
                    cursor.moveToNext();
                }

                cursor.close();

            }
        }
    return Contacts;
    }

    //search contact by its unique id
    @SuppressLint("Range")
    public Contact searchContact(int contactID){
        Contact contact=null;
        String[] parameters = new String[]{String.valueOf(contactID)};
        Cursor cursor = getReadableDatabase().rawQuery("SELECT id,first_name,last_name,email,telephone,gender FROM contact WHERE id=?",parameters);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String gender = cursor.getString(cursor.getColumnIndex("gender"));
                contact = new Contact(firstName, lastName, email, telephone,gender );
            }
            Log.d("INFO",contact.toString());
            cursor.close();
        }
        return contact;
    }



    public long insertContact(String userID, String firstName, String lastName, String fullName, String telephone, String email, String gender){

        ContentValues values = new ContentValues();
        values.put("user_id",userID);
        values.put("first_name",firstName);
        values.put("last_name",lastName);
        values.put("first_name_last_name",fullName);
        values.put("telephone",telephone);
        values.put("email",email);
        values.put("gender",gender);
        long rowID = getWritableDatabase().insert("contact",null,values);

        return rowID;
    }






    public long updateContact(int contactID, ContentValues contentValues ){
        String[] parameters = new String[]{String.valueOf(contactID)};
        int rowID = getWritableDatabase().update("contact",contentValues,"id=?",parameters);
        return rowID;
    }

    public long deleteContact(int contactID){
        String[] parameters = new String[]{String.valueOf(contactID)};
        int rowID = getWritableDatabase().delete("contact","id=?",parameters);
        return rowID;
    }


    public long insertUser(String email, String password){
        long userID;
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("password",password);

        //insert will return the rowID which is the userID hence we do need delete users
        userID = getWritableDatabase().insert("user",null,contentValues);
        return userID;
    }









}

