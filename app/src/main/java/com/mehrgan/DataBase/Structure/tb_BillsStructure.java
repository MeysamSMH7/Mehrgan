package com.mehrgan.DataBase.Structure;

public class tb_BillsStructure {

    public static final String tableName = "tb_Bills";

    public static final String colPK_Bill="PK_Bill";
    public static final String colfullName = "fullName";
    public static final String colphoneNum = "phoneNum";
    public static final String coldocName = "docName";
    public static final String colrightDoor = "rightDoor";
    public static final String colrightNazdik = "rightNazdik";
    public static final String colleftDoor = "leftDoor";
    public static final String colleftNazdik = "leftNazdik";
    public static final String coldescription = "description";
    public static final String coldateMiladi = "dateMiladi";
    public static final String coldateJalali = "dateJalali";

    public static String createTableQuery = "create table " + tableName + "(" +
            colPK_Bill + " text primary key , " +
            colfullName + " text, " +
            colphoneNum + " text, " +
            coldocName + " text, " +
            colrightDoor + " text, " +
            colrightNazdik + " text, " +
            colleftDoor + " text, " +
            colleftNazdik + " text, " +
            coldescription + " text, " +
            coldateMiladi + " current_date, " +
            coldateJalali + " current_date" +
            ")";
}
