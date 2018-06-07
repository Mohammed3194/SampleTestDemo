package mohammedzaheeruddin.sampletest.entity;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public class DisplayItem {

    private String City_Name;
    private String City_Id;

    public DisplayItem(){}

    public DisplayItem(String id, String city){
        this.City_Id = id;
        this.City_Name = city;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }

    public String getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(String city_Id) {
        City_Id = city_Id;

    }
}
