package mohammedzaheeruddin.sampletest.db;

import java.util.ArrayList;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public class DaoUtils {

    /*
     * Sqlite Data base and table declaration
     */

    public static final String DATABASE_NAME = "sample.db.ver.0.1";
    public static final int DATABASE_VERSION = 1;

    // City Table
    public static final String DATABASE_CITY_DETAILS_TABLE_NAME = "cities_details_table";

    /*
    * City table field declaration
    */
    public static final String CITY_ID = "CITY_ID";
    public static final String CITY_NAME = "CITY_NAME";

    protected static ArrayList<String> mCityDetailsTable = null;

    public static void initCityDetailsList() {
        mCityDetailsTable = new ArrayList<String>();
        mCityDetailsTable.add(CITY_ID);
        mCityDetailsTable.add(CITY_NAME);
    }

    //  method for initialization of filled in city table. .
    public static ArrayList<String> getCityTableColumns() {
        if ((mCityDetailsTable == null) || (mCityDetailsTable.size() == 0)) {
            initCityDetailsList();
        }
        return mCityDetailsTable;
    }

}
