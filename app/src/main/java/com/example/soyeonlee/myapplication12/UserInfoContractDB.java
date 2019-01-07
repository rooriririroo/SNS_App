package com.example.soyeonlee.myapplication12;

public class UserInfoContractDB {

    private UserInfoContractDB() {};
    private static final String TABLE_NAME = "USER_INFO";
    private static final String _ID = "_ID"; // [0]
    private static final String COL_NAME = "USER_NAME"; // [1]
    private static final String COL_ID = "USER_ID"; // [2]
    private static final String COL_PW = "USER_PW"; // [3]
    private static final String COL_BIRTH = "USER_BIRTH"; // [4]
    private static final String COL_PHONE = "USER_PHONE"; // [5]
    private static final String COL_IMAGE = "USER_IMAGE"; // [6]
    private static final String COL_NICKNAME = "USER_NICKNAME"; // [7]
    private static final String COL_DATE = "USER_DATE"; // [8]


    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " "
            + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+ ", " +  COL_NAME + " TEXT" + ", "
            + COL_ID + " TEXT" + ", " + COL_PW + " TEXT" + ", " + COL_BIRTH + " TEXT" +", "
            + COL_PHONE + " TEXT" + ", " + COL_IMAGE + " TEXT" + ", " + COL_NICKNAME + " TEXT" + ", "
            + COL_DATE + " TEXT" + ")";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS" + " " + TABLE_NAME;
    //public static final String SQL_SELECT = "SELECT * FROM " + TABLE_NAME +" ORDER BY " + COL_DATE + " DESC" + "," + _ID + " DESC";
    public static final String SQL_SELECT = "SELECT * FROM " + TABLE_NAME;
    //public static final String SQL_INSERT = "INSERT OR REPLACE INTO" + TABLE_NAME;
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TABLE_NAME + " "
            + "(" + COL_NAME + ", " + COL_ID + ", " + COL_PW + ", " + COL_BIRTH + ", " + COL_PHONE
            + ", " + COL_IMAGE + ", " + COL_NICKNAME + ", " + COL_DATE + ") VALUES";
    //public static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME;
    public static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME +" WHERE " + _ID + " = ";
    public static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET";
}
