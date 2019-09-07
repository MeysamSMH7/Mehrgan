package com.mehrgan.Classes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;


import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelToSQLite {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private Context mContext;
    private SQLiteDatabase database;
    private String mDbName;
    private boolean dropTable = true;

    public ExcelToSQLite(Context context, String dbName) {
        mContext = context;
        mDbName = dbName;
        try {
            database = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath(mDbName).getAbsolutePath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExcelToSQLite(Context context, String dbName, boolean dropTable) {
        mContext = context;
        mDbName = dbName;
        this.dropTable = dropTable;
        try {
            database = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath(mDbName).getAbsolutePath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importFromAsset(final String assetFileName, final ImportListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    working(mContext.getAssets().open(assetFileName));
                    if (listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onCompleted(mDbName);
                            }
                        });
                    }
                } catch (final Exception e) {
                    if (database != null && database.isOpen()) {
                        database.close();
                    }
                    if (listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(e);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void importFromFile(String filePath, ImportListener listener) {
        importFromFile(new File(filePath), listener);
    }

    private void importFromFile(final File file, final ImportListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    working(new FileInputStream(file));
                    if (listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onCompleted(mDbName);
                            }
                        });
                    }
                } catch (final Exception e) {
                    if (database != null && database.isOpen()) {
                        database.close();
                    }
                    if (listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(e);
                            }
                        });
                    }
                }
            }
        }).start();

    }

    private void working(InputStream stream) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(stream, true);
        int sheetNumber = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNumber; i++) {
            createTable(workbook.getSheetAt(i));
        }
        database.close();
    }

    private void createTable(Sheet sheet) {
        Cursor cursor = null;
        try {
            StringBuilder createTableSql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            createTableSql.append(sheet.getSheetName());
            createTableSql.append("(");
            Iterator<Row> rit = sheet.rowIterator();
            Row rowHeader = rit.next();
            List<String> columns = new ArrayList<>();
            for (int i = 0; i < rowHeader.getPhysicalNumberOfCells(); i++) {
                createTableSql.append(rowHeader.getCell(i).getStringCellValue());
                if (i == rowHeader.getPhysicalNumberOfCells() - 1) {
                    createTableSql.append(" TEXT");
                } else {
                    createTableSql.append(" TEXT,");
                }
                columns.add(rowHeader.getCell(i).getStringCellValue());
            }
            createTableSql.append(")");

            if (dropTable) {
                //database.execSQL("DROP TABLE IF EXISTS " + sheet.getSheetName());
                tb_BillsDataSource tb_billsDataSource = new tb_BillsDataSource(mContext);
                tb_billsDataSource.Open();
                tb_billsDataSource.DeleteAll();
                tb_billsDataSource.Close();
            }

            database.execSQL(createTableSql.toString());

            for (String column : columns) {
                cursor = database.rawQuery("SELECT * FROM " + sheet.getSheetName(), null);
                // grab cursor for all data
                int deleteStateColumnIndex = cursor.getColumnIndex(column);  // see if the column is there
                if (deleteStateColumnIndex < 0) {
                    String type = "TEXT";
                    // missing_column not there - add it
                    database.execSQL("ALTER TABLE " + sheet.getSheetName()
                            + " ADD COLUMN " + column + " " + type + " NULL;");
                }
            }

            while (rit.hasNext()) {
                Row row = rit.next();
                ContentValues values = new ContentValues();
                int numOfCells = row.getPhysicalNumberOfCells();
                for (int n = 0; n < columns.size(); n++) {
                    if (row.getCell(n) == null) {
                        values.put(columns.get(n), "");
                    } else if (row.getCell(n).getCellTypeEnum() == CellType.NUMERIC) {
                        values.put(columns.get(n), row.getCell(n).getNumericCellValue());
                    } else {
                        values.put(columns.get(n), row.getCell(n).getStringCellValue());
                    }
                }
                long result = database.insert(sheet.getSheetName(),
                        null, values);
                if (result < 0) {
                    throw new RuntimeException("وارد کردن با مشکل روبه رو شد!");
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public interface ImportListener {
        void onStart();

        void onCompleted(String dbName);

        void onError(Exception e);
    }

}