package mohammedzaheeruddin.sampletest.retrofit;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public class ApiUtils {

    public static final String BASE_URL = ServiceConstants.BASE_URL;

    private ApiUtils() {}

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
