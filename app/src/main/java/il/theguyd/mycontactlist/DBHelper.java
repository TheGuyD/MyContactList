package il.theguyd.mycontactlist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import il.theguyd.mycontactlist.Models.Contact;
import il.theguyd.mycontactlist.Models.User;

public class DBHelper extends SQLiteOpenHelper {
    //TODO: all database operations should acure in here!!!!!!
    //TODO: implement update,add,delete
    private static final String DB_NAME = "database1";
    private static final int DB_VERSION = 1;

    User user = null;



    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table named user with column (id,first_name,last_name,email,password,telephone)
        String sqlStatementCreateUserTable = "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT , first_name TEXT NOT NULL , last_name TEXT NOT NULL , email TEXT NOT NULL UNIQUE , telephone TEXT , password TEXT NOT NULL )";

        //create table named contact with column (id,user_id,first_name,last_name,first_name_last_name,gender,email,telephone)
        String sqlStatementCreateContactTable = "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT , user_id  INTEGER , first_name TEXT NOT NULL , last_name TEXT NOT NULL , first_name_last_name  TEXT , gender TEXT , email TEXT , telephone TEXT NOT NULL , FOREIGN KEY (user_id) REFERENCES user(id) )";

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

    //search user query for userID based on email and password
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

    @SuppressLint("Range")
    public ArrayList<Contact> getAllUserContacts(int userID) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String[] s = new String[]{String.valueOf(userID)};
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT first_name,last_name,email,telephone FROM contact WHERE user_id=?", s);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                    String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                    String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    contacts.add(new Contact(firstName, lastName, firstName + " " + lastName, email, telephone));

                    //move to the next row in the database
                    cursor.moveToNext();
                }
                cursor.close();
            }

        }
        return contacts;
    }

    @SuppressLint("Range")
    public ArrayList<Contact> searchUserContacts(String name,int userID) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String firstNameLastName = "%" + name + "%";
        String[] s = new String[]{String.valueOf(userID), firstNameLastName};
        Cursor cursor = getReadableDatabase().rawQuery("SELECT first_name,last_name,email,telephone FROM contact WHERE user_id=? AND first_name_last_name LIKE ? ", s);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                    String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                    String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    contacts.add(new Contact(firstName, lastName, firstName + " " + lastName, email, telephone));

                    //move to the next row in the database
                    cursor.moveToNext();
                }

                cursor.close();

            }
        }
    return contacts;
    }



}

