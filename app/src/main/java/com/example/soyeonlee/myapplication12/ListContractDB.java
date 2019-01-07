package com.example.soyeonlee.myapplication12;

public class ListContractDB {

    private ListContractDB() {};
    private static final String TABLE_NAME = "MEMO_LIST";
    private static final String _ID = "_ID"; // [0]
    private static final String COL_DATE = "DATE"; // [1]
    private static final String COL_WRITE = "WRITE"; // [2]
    private static final String COL_IMAGE = "IMAGE"; // [3]
    //private static final String COL_VIDEO = "VIDEO"; // [4]
    //private static final String COL_FILE = "FILE"; // [5]



    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
            + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+ ", " +  COL_DATE + " TEXT" + ", "
            + COL_WRITE + " TEXT" + ", " + COL_IMAGE + " TEXT" + ")";
    //public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
      //      + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+ ", " +  COL_DATE + " TEXT" + ", "
        //    + COL_WRITE + " TEXT" + ", " + COL_IMAGE + " TEXT" + COL_VIDEO + " TEXT" + ", " + COL_FILE + " TEXT" + ")";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS" + " " + TABLE_NAME;
    public static final String SQL_SELECT = "SELECT * FROM " + TABLE_NAME +" ORDER BY " + COL_DATE + " DESC" + "," + _ID + " DESC";
    //public static final String SQL_SELECT = "SELECT * FROM " + TABLE_NAME;
    //public static final String SQL_INSERT = "INSERT OR REPLACE INTO" + TABLE_NAME;

    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TABLE_NAME + " "
            + "(" + COL_DATE + ", " + COL_WRITE + ", " + COL_IMAGE + ") VALUES";
    //public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TABLE_NAME + " "
      //      + "(" + COL_DATE + ", " + COL_WRITE + ", " + COL_IMAGE + ", "  + COL_VIDEO + ", " + COL_FILE + ") VALUES";
    //public static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME;
    public static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME +" WHERE " + _ID + " = ";
    public static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET";
}
