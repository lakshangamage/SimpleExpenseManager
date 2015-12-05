package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;

/**
 * Created by hp pc on 2015-12-04.
 */
public class DBHandler extends SQLiteOpenHelper{
    private static final String DB_NAME = "130166P";
    public DBHandler(Context context){
        super(context, DB_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account(account_no VARCHAR(10) PRIMARY KEY,bank_name VARCHAR(50),account_holder_name VARCHAR(100), balance DECIMAL(16,2))");
        /*
        *
        * Expense type = 0 for income, 1 for expense
        *
        *
         */
        db.execSQL("CREATE TABLE transaction(transaction_id INT(10) PRIMARY KEY AUTOINCREMENT,date DATE,account_no VARCHAR(10), expense_type INT(1),amount DECIMAL(16,2), FOREIGN KEY(account_no) REFERENCES account(acount_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS transaction");
        onCreate(db);
    }

    public boolean setData(String table,ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.insert(table,null,contentValues) >= 0){
            return true;
        }
        return false;
    }
    public Cursor getData(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery( query, null );
    }
    public int deleteData(String table, String whereClause, String[] whereArgs ){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, whereClause, whereArgs);
    }
    public int updateData(String table, ContentValues contentValues,String whereClause, String[] whereArgs ){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(table,contentValues,whereClause,whereArgs);
    }
}
