package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by hp pc on 2015-12-04.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBHandler dbHandler = null;
    public PersistentTransactionDAO(Context context) {
        dbHandler = new DBHandler(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        ContentValues accountValues = new ContentValues();
        accountValues.put("date",strDate);
        if(expenseType == ExpenseType.EXPENSE){
            accountValues.put("expense_type",1);
        }else{
            accountValues.put("expense_type",0);
        }
        accountValues.put("amount",amount);
        accountValues.put("account_no", accountNo);
        dbHandler.setData("transact", accountValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        Cursor cursor = dbHandler.getData("SELECT * FROM transact");
        while(cursor.moveToNext()){
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatter.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExpenseType expenseType =ExpenseType.EXPENSE;
            if(cursor.getInt(3) == 0 ){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(date,cursor.getString(2),expenseType,cursor.getDouble(4));
            transactionList.add(transaction);
        }
        if(!cursor.isClosed()){
            cursor.close();
        }

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        Cursor cursor = dbHandler.getData("SELECT * FROM transact LIMIT "+limit);
        while(cursor.moveToNext()){
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatter.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExpenseType expenseType =ExpenseType.EXPENSE;
            if(cursor.getInt(3) == 0 ){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(date,cursor.getString(2),expenseType,cursor.getDouble(4));
            transactionList.add(transaction);
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return transactionList;
    }

}
