package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * Created by hp pc on 2015-12-04.
 */
public class PersistentAccountDAO implements AccountDAO {
    DBHandler dbHandler = null;
    public PersistentAccountDAO(Context context) {
        dbHandler = new DBHandler(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> numberList = new ArrayList<>();
        Cursor cursor = dbHandler.getData("SELECT account_no FROM account");
        while(cursor.moveToNext()){
            numberList.add(cursor.getString(cursor.getColumnIndex("account_no")));
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return numberList;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accountList = new ArrayList<>();
        Cursor cursor = dbHandler.getData("SELECT * FROM account");
        while(cursor.moveToNext()){
            Account account = new Account(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4));
            accountList.add(account);
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = dbHandler.getData("SELECT * FROM account WHERE account_no = '"+accountNo+"'");
        if(cursor.moveToFirst()){
            Account account = new Account(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getDouble(4));
            if(!cursor.isClosed()){
                cursor.close();
            }
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues accountValues = new ContentValues();
        accountValues.put("account_no",account.getAccountNo());
        accountValues.put("bank_name",account.getBankName());
        accountValues.put("account_holder_name",account.getAccountHolderName());
        accountValues.put("balance",account.getBalance());
        dbHandler.setData("account",accountValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String whereClause = "account_no = ?";
        String[] args={accountNo};
        if (dbHandler.deleteData("account",whereClause,args) < 1) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        ContentValues accountValues = new ContentValues();
        Account account = getAccount(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        accountValues.put("balance", account.getBalance());
        String whereClause = "account_no = ?";
        String[] args={accountNo};
        dbHandler.updateData("account", accountValues, whereClause, args);
    }
}

