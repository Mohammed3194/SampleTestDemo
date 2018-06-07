package mohammedzaheeruddin.sampletest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public class OpenHelper extends SQLiteOpenHelper {

    public OpenHelper(Context context) {
        super(context,
                DaoUtils.DATABASE_NAME,
                null,
                DaoUtils.DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        // Creating City Details Table ...
        ArrayList<String> lColumns = DaoUtils.getCityTableColumns();

        StringBuffer lUserDetailsTable = new StringBuffer();
        lUserDetailsTable.append("CREATE TABLE ");
        lUserDetailsTable.append(DaoUtils.DATABASE_CITY_DETAILS_TABLE_NAME);
        lUserDetailsTable.append(" (id INTEGER PRIMARY KEY");

        for (int i = 0; i < lColumns.size(); i++) {
            lUserDetailsTable.append(", ");
            lUserDetailsTable.append(lColumns.get(i));
            lUserDetailsTable.append(" TEXT");

        }
        lUserDetailsTable.append(")");
        db.execSQL(lUserDetailsTable.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        // TODO Auto-generated method stub

    }
}
