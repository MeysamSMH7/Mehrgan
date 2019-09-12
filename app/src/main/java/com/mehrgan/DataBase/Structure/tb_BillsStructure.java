package com.mehrgan.DataBase.Structure;

public class tb_BillsStructure {

    public static final String tableName = "tb_Bills";

    public static final String PK_Bill="PK_Bill";
    public static final String fullName = "fullName";
    public static final String phoneNum = "phoneNum";
    public static final String docName = "docName";
    public static final String dorOD = "dorOD";
    public static final String dorOS = "dorOS";
    public static final String dorPD = "dorPD";
    public static final String dorAdd = "dorAdd";
    public static final String nazOD = "nazOD";
    public static final String nazOS = "nazOS";
    public static final String nazPD = "nazPD";
    public static final String description = "description";
    public static final String dateMiladi = "dateMiladi";
    public static final String dateJalali = "dateJalali";

    public static String createTableQuery = "create table " + tableName + "(" +
            PK_Bill + " text primary key , " +
            fullName + " text, " +
            phoneNum + " text, " +
            docName + " text, " +
            dorOD + " text, " +
            dorOS + " text, " +
            dorPD + " text, " +
            dorAdd + " text, " +
            nazOD + " text, " +
            nazOS + " text, " +
            nazPD + " text, " +
            description + " text, " +
            dateMiladi + " current_date, " +
            dateJalali + " current_date" +
            ")";
}
