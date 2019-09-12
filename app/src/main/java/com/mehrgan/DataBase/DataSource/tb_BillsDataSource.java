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

            tb_BillsStructure.PK_Bill,
            tb_BillsStructure.fullName,
            tb_BillsStructure.phoneNum,
            tb_BillsStructure.docName,
            tb_BillsStructure.dorOD,
            tb_BillsStructure.dorOS,
            tb_BillsStructure.dorPD,
            tb_BillsStructure.dorAdd,
            tb_BillsStructure.nazOD,
            tb_BillsStructure.nazOS,
            tb_BillsStructure.nazPD,
            tb_BillsStructure.description,
            tb_BillsStructure.dateMiladi,
            tb_BillsStructure.dateJalali


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
        values.put(tb_BillsStructure.PK_Bill, data.PK_Bill);
        values.put(tb_BillsStructure.fullName, data.fullName);
        values.put(tb_BillsStructure.phoneNum, data.phoneNum);
        values.put(tb_BillsStructure.docName, data.docName);
        values.put(tb_BillsStructure.dorOD, data.dorOD);
        values.put(tb_BillsStructure.dorOS, data.dorOS);
        values.put(tb_BillsStructure.dorPD, data.dorPD);
        values.put(tb_BillsStructure.dorAdd, data.dorAdd);
        values.put(tb_BillsStructure.nazOD, data.nazOD);
        values.put(tb_BillsStructure.nazOS, data.nazOS);
        values.put(tb_BillsStructure.nazPD, data.nazPD);
        values.put(tb_BillsStructure.description, data.description);
        values.put(tb_BillsStructure.dateMiladi, data.dateMiladi);
        values.put(tb_BillsStructure.dateJalali, data.dateJalali);
        long res = database.insert(tb_BillsStructure.tableName, null, values);
        Close();

        return res;
    }

    public long EditItems(tb_Bills data) {
        Open();
        ContentValues values = new ContentValues();
        values.put(tb_BillsStructure.PK_Bill, data.PK_Bill);
        values.put(tb_BillsStructure.fullName, data.fullName);
        values.put(tb_BillsStructure.phoneNum, data.phoneNum);
        values.put(tb_BillsStructure.docName, data.docName);
        values.put(tb_BillsStructure.dorOD, data.dorOD);
        values.put(tb_BillsStructure.dorOS, data.dorOS);
        values.put(tb_BillsStructure.dorPD, data.dorPD);
        values.put(tb_BillsStructure.dorAdd, data.dorAdd);
        values.put(tb_BillsStructure.nazOD, data.nazOD);
        values.put(tb_BillsStructure.nazOS, data.nazOS);
        values.put(tb_BillsStructure.nazPD, data.nazPD);
        values.put(tb_BillsStructure.description, data.description);
        values.put(tb_BillsStructure.dateMiladi, data.dateMiladi);
        values.put(tb_BillsStructure.dateJalali, data.dateJalali);
        long res = database.update(tb_BillsStructure.tableName,
                values,
                tb_BillsStructure.PK_Bill + "= '" + data.PK_Bill + "'",
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
                tb_BillsStructure.PK_Bill + "= '" + id + "'",
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
        database.delete(tb_BillsStructure.tableName, tb_BillsStructure.PK_Bill + " = " + id, null);
    }

    public List<tb_Bills> GetList() {
        List<tb_Bills> lstData = new ArrayList<tb_Bills>();

        Cursor cursor = database.query(tb_BillsStructure.tableName,
                allColumns,
                null, null, null, null,
                tb_BillsStructure.dateMiladi + " DESC");
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
                tb_BillsStructure.dateMiladi + " BETWEEN '" + startDate + "' AND '" + endDate + "'", null, null, null,
                tb_BillsStructure.dateMiladi + " DESC");
        cursor.moveToFirst();


//        "select * from tb_Bills where dateMiladi between 'date' and 'date' AND Pk = pk AND name =  'سید' AND"
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
                tb_BillsStructure.dateMiladi + " DESC");
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
                values.put(tb_BillsStructure.dateMiladi, tool.getGregorianDate());
                database.update(tb_BillsStructure.tableName,
                        values,
                        tb_BillsStructure.PK_Bill + "= '" + list.get(i).PK_Bill + "'",
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
        data.dorOD = cursor.getString(4);
        data.dorOS = cursor.getString(5);
        data.dorPD = cursor.getString(6);
        data.dorAdd = cursor.getString(7);
        data.nazOD = cursor.getString(8);
        data.nazOS = cursor.getString(9);
        data.nazPD = cursor.getString(10);
        data.description = cursor.getString(11);
        data.dateMiladi = cursor.getString(12);
        data.dateJalali = cursor.getString(13);

        return data;
    }

}
