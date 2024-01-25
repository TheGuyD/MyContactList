package il.theguyd.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //TODO: all database operations should acuure in here!!!!!!
    //TODO: implement search,update,add,delete
    private static final String DB_NAME = "database1";
    private static final int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table named user with column (id,first_name,last_name,email,password,telephone)
        String sqlStatementCreateUserTable="CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT , first_name TEXT NOT NULL , last_name TEXT NOT NULL , email TEXT NOT NULL UNIQUE , telephone TEXT , password TEXT NOT NULL )";

        //create table named contact with column (id,user_id,first_name,last_name,first_name_last_name,gender,email,telephone)
        String sqlStatementCreateContactTable="CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT , user_id  INTEGER , first_name TEXT NOT NULL , last_name TEXT NOT NULL , first_name_last_name  TEXT , gender TEXT , email TEXT , telephone TEXT NOT NULL , FOREIGN KEY (user_id) REFERENCES user(id) )";

        //execute creation
        db.execSQL(sqlStatementCreateUserTable);
        db.execSQL(sqlStatementCreateContactTable);

        initDatabase(db);
    }

    private void initDatabase(SQLiteDatabase db) {
        ContentValues firstUser = new ContentValues();

        firstUser.put("first_name","Guy");
        firstUser.put("last_name","David");
        firstUser.put("telephone","0525517418");
        firstUser.put("email","gaidavid1@gmail.com");
        firstUser.put("password","123456");

        long userID1 = db.insert("user",null,firstUser);

        ContentValues secondUser = new ContentValues();

        secondUser.put("first_name","Danny");
        secondUser.put("last_name","Danon");
        secondUser.put("telephone","0582224653");
        secondUser.put("email","DannyDa@gmail.com");
        secondUser.put("password","123456");

        long userID2 = db.insert("user",null,secondUser);



        ContentValues firstContact = new ContentValues();


        firstContact.put("user_id",userID1);
        firstContact.put("first_name","meisam");
        firstContact.put("last_name","abdul");
        firstContact.put("first_name_last_name","meisam abdul");
        firstContact.put("gender","male");
        firstContact.put("telephone","097433225");
        firstContact.put("email","meisam@arduan.com");


        db.insert("contact",null,firstContact);

        ContentValues secondContact = new ContentValues();


        secondContact.put("user_id",userID1);
        secondContact.put("first_name","mubarak");
        secondContact.put("last_name","husein");
        secondContact.put("first_name_last_name","mubarak husein");
        secondContact.put("gender","male");
        secondContact.put("telephone","06666666");
        secondContact.put("email","alla@wakbar.com");


        db.insert("contact",null,secondContact);

        ContentValues thirdContact = new ContentValues();


        thirdContact.put("user_id",userID2);
        thirdContact.put("first_name","bamba");
        thirdContact.put("last_name","nugat");
        thirdContact.put("first_name_last_name","bamba nugat");
        thirdContact.put("gender","male");
        thirdContact.put("telephone","055323232");
        thirdContact.put("email","meisam@meisam.com");


        db.insert("contact",null,thirdContact);

        ContentValues fourthContact = new ContentValues();


        fourthContact.put("user_id",userID2);
        fourthContact.put("first_name","muhammad");
        fourthContact.put("last_name","deff");
        fourthContact.put("first_name_last_name","muhammad deff");
        fourthContact.put("gender","male");
        fourthContact.put("telephone","6666666");
        fourthContact.put("email","alla@hammas.com");


        db.insert("contact",null,fourthContact);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
