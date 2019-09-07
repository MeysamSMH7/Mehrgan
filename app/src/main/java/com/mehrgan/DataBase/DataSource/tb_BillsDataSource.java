package com.mehrgan.DataBase.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mehrgan.Classes.CalendarTool;
import com.mehrgan.DataBase.DatabaseManagement;
import com.mehrgan.DataBase.Structure.tb_BillsStructure;
import com.mehrgan.DataBase.Tables.tb_Bills;

import java.util.ArrayList;
import java.util.List;

public class tb_BillsDataSource {

    private SQLiteDatabase database;
    private DatabaseManagement dbManagement;


    private String[] allColumns = {

            tb_BillsStructure.colPK_Bill,
            tb_BillsStructure.colfullName,
            tb_BillsStructure.colphoneNum,
            tb_BillsStructure.coldocName,
            tb_BillsStructure.colrightDoor,
            tb_BillsStructure.colrightNazdik,
            tb_BillsStructure.colleftDoor,
            tb_BillsStructure.colleftNazdik,
            tb_BillsStructure.coldescription,
            tb_BillsStructure.coldateMiladi,
            tb_BillsStructure.coldateJalali


    };

    public tb_BillsDataSource(Context context) {
        dbManagement = new DatabaseManagement(context);
    }

    public void Open() throws SQLException {
        database = dbManagement.getWritableDatabase();
    }

    public void Close() {
        dbManagement.close();
        database.close();
    }

    public long Add(tb_Bills data) {

        Open();
        ContentValues values = new ContentValues();
        values.put(tb_BillsStructure.colPK_Bill, data.PK_Bill);
        values.put(tb_BillsStructure.colfullName, data.fullName);
        values.put(tb_BillsStructure.colphoneNum, data.phoneNum);
        values.put(tb_BillsStructure.coldocName, data.docName);
        values.put(tb_BillsStructure.colrightDoor, data.rightDoor);
        values.put(tb_BillsStructure.colrightNazdik, data.rightNazdik);
        values.put(tb_BillsStructure.colleftDoor, data.leftDoor);
        values.put(tb_BillsStructure.colleftNazdik, data.leftNazdik);
        values.put(tb_BillsStructure.coldescription, data.description);
        values.put(tb_BillsStructure.coldateMiladi, data.dateMiladi);
        values.put(tb_BillsStructure.coldateJalali, data.dateJalali);
        long res = database.insert(tb_BillsStructure.tableName, null, values);
        Close();

        return res;
    }

    public long EditItems(tb_Bills data) {
        Open();
        ContentValues values = new ContentValues();
        values.put(tb_BillsStructure.colPK_Bill, data.PK_Bill);
        values.put(tb_BillsStructure.colfullName, data.fullName);
        values.put(tb_BillsStructure.colphoneNum, data.phoneNum);
        values.put(tb_BillsStructure.coldocName, data.docName);
        values.put(tb_BillsStructure.colrightDoor, data.rightDoor);
        values.put(tb_BillsStructure.colrightNazdik, data.rightNazdik);
        values.put(tb_BillsStructure.colleftDoor, data.leftDoor);
        values.put(tb_BillsStructure.colleftNazdik, data.leftNazdik);
        values.put(tb_BillsStructure.coldescription, data.description);
        values.put(tb_BillsStructure.coldateMiladi, data.dateMiladi);
        values.put(tb_BillsStructure.coldateJalali, data.dateJalali);
        long res = database.update(tb_BillsStructure.tableName,
                values,
                tb_BillsStructure.colPK_Bill + "= '" + data.PK_Bill + "'",
                null);
        Close();

        return res;
    }

    public long QueryNumEntries() {
        return DatabaseUtils.queryNumEntries(database, tb_BillsStructure.tableName);
    }


    public tb_Bills GetRecord() {
        Cursor cursor = database.query(tb_BillsStructure.tableName, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return null;
        }

        tb_Bills data = ConvertToRecord(cursor);
        cursor.close();
        return data;
    }


    public tb_Bills GetRecord(String id) {
        Cursor cursor = database.query(tb_BillsStructure.tableName, allColumns,
                tb_BillsStructure.colPK_Bill + "= '" + id + "'",
                null, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            return null;
        }

        tb_Bills data = ConvertToRecord(cursor);
        cursor.close();
        return data;
    }


    public void DeleteAll() {
        database.delete(tb_BillsStructure.tableName, null, null);
    }

    public void DeleteById(String id) {
        database.delete(tb_BillsStructure.tableName, tb_BillsStructure.colPK_Bill + " = " + id, null);
    }

    public List<tb_Bills> GetList() {
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                null, null, null, null,
                tb_BillsStructure.coldateMiladi + " DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        return lstData;
    }

    public List<tb_Bills> GetListByDate(String startDate, String endDate) {
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                tb_BillsStructure.coldateMiladi + " BETWEEN '" + startDate + "' AND '" + endDate + "'", null, null, null,
                tb_BillsStructure.coldateMiladi + " DESC");
        cursor.moveToFirst();


//        "select * from tb_Bills where dateMiladi between 'date' and 'date' AND colPk = pk AND name =  'سید' AND"
//        "Phone = '999'";

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        return lstData;
    }

    public List<tb_Bills> Search(ArrayList array) {
        Open();
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        String query = "";
        if (array.size() == 1) {
            query = array.get(0).toString();
        } else {
            for (int i = 0; i < array.size(); i++) {
                query += array.get(i).toString();
                if (i < array.size() - 1) {
                    query += " AND ";
                }
            }
        }
        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                query, null, null, null,
                tb_BillsStructure.coldateMiladi + " DESC");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            tb_Bills tmpInfo = ConvertToRecord(cursor);
            lstData.add(tmpInfo);
            cursor.moveToNext();
        }

        cursor.close();
        Close();
        return lstData;
    }

    public void IraniToGery() {

        List<tb_Bills> list = GetList();


        for (int i = 0; i < list.size(); i++) {

            if (!list.get(i).dateJalali.equals("")) {
                String[] date = list.get(i).dateJalali.split("/");

                CalendarTool tool = new CalendarTool();
                tool.setIranianDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));


                ContentValues values = new ContentValues();
                values.put(tb_BillsStructure.coldateMiladi, tool.getGregorianDate());
                database.update(tb_BillsStructure.tableName,
                        values,
                        tb_BillsStructure.colPK_Bill + "= '" + list.get(i).PK_Bill + "'",
                        null);
            }
        }

    }


    private tb_Bills ConvertToRecord(Cursor cursor) {
        tb_Bills data = new tb_Bills();


        data.PK_Bill = cursor.getString(0);
        data.fullName = cursor.getString(1);
        data.phoneNum = cursor.getString(2);
        data.docName = cursor.getString(3);
        data.rightDoor = cursor.getString(4);
        data.rightNazdik = cursor.getString(5);
        data.leftDoor = cursor.getString(6);
        data.leftNazdik = cursor.getString(7);
        data.description = cursor.getString(8);
        data.dateMiladi = cursor.getString(9);
        data.dateJalali = cursor.getString(10);

        return data;
    }

}
