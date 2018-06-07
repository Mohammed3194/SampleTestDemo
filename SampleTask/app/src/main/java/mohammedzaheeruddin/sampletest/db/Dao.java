package mohammedzaheeruddin.sampletest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mohammedzaheeruddin.sampletest.entity.DisplayItem;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public class Dao {

    protected Context mContext;
    protected static SQLiteDatabase mSqlDatabase;
    protected SQLiteStatement mInsertStatement;
    static OpenHelper lOpenHelper;

    public Dao(Context context) {
        mContext = context;
        closeDatabase();
        lOpenHelper = new OpenHelper(mContext);
        mSqlDatabase = lOpenHelper.getWritableDatabase();
    }

    // insert statement to insert fields in city table . .
    public String getInsertSqlCityDetails() {
        ArrayList<String> lColumns = DaoUtils.getCityTableColumns();
        StringBuffer lBuffer = new StringBuffer();
        lBuffer.append("insert into  ");
        lBuffer.append(DaoUtils.DATABASE_CITY_DETAILS_TABLE_NAME);
        lBuffer.append("(");
        for (int i = 0; i < lColumns.size(); i++) {
            lBuffer.append(lColumns.get(i));
            if (i < (lColumns.size() - 1)) {
                lBuffer.append(",");
            }
        }
        lBuffer.append(")");
        lBuffer.append(" values (");
        for (int i = 0; i < lColumns.size(); i++) {
            lBuffer.append("?");
            if (i < lColumns.size() - 1) {
                lBuffer.append(",");
            }

        }
        lBuffer.append(");");

        return lBuffer.toString();

    }

    public void insertCityDetailsData(String cityId,
                                       String cityName){
        if (!cityIdExist(cityId)) {
            mSqlDatabase = lOpenHelper.getWritableDatabase();
            mInsertStatement = mSqlDatabase.compileStatement(getInsertSqlCityDetails());
            mInsertStatement.bindString(1,
                    cityId);
            mInsertStatement.bindString(2,
                    cityName);
            mInsertStatement.executeInsert();
        }else {
            mSqlDatabase = lOpenHelper.getWritableDatabase();
            ArrayList<String> lColumns = DaoUtils.getCityTableColumns();
            ContentValues conValue = new ContentValues();
            conValue.put(lColumns.get(0),
                    cityId);
            conValue.put(lColumns.get(1),
                    cityName);
            mSqlDatabase.update(DaoUtils.DATABASE_CITY_DETAILS_TABLE_NAME,
                    conValue,
                    DaoUtils.CITY_ID + "=?",
                    new String[]{cityId});
        }
        mSqlDatabase.close();
    }

    public List<DisplayItem> getCityDetailsInDescendingOrder(){
        List<DisplayItem> allRecords = new ArrayList<DisplayItem>();

        String selectQuery = "SELECT * FROM cities_details_table " ;
        mSqlDatabase = lOpenHelper.getReadableDatabase();
        Cursor lCursor = mSqlDatabase.rawQuery( selectQuery,
                null );

        DisplayItem displayItem = null;
        if( lCursor.moveToFirst()){
            do {
                displayItem = new DisplayItem();
                displayItem.setCity_Id(lCursor.getString(1));
                displayItem.setCity_Name(lCursor.getString(2));
                allRecords.add(displayItem);
            }
            while( lCursor.moveToNext() );
        }
        lCursor.close();
        return allRecords;
    }

    // Method to check record is exist or not if exist then returning true
    public boolean cityIdExist(String cityId) {
        mSqlDatabase = lOpenHelper.getReadableDatabase();
        Cursor mCursor = mSqlDatabase.rawQuery("SELECT * FROM "
                        + DaoUtils.DATABASE_CITY_DETAILS_TABLE_NAME
                        + " WHERE "
                        + DaoUtils.CITY_ID
                        + "=?",
                new String[]{cityId});
        if (mCursor.moveToFirst()) {
            mSqlDatabase.close();
            return true;
        } else {
            mSqlDatabase.close();
            return false;
        }
    }

    // Method to close database if it is open
    public void closeDatabase() {
        if (mSqlDatabase != null && mSqlDatabase.isOpen()) {
            mSqlDatabase.close();
        }
    }
}
