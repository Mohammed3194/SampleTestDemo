package mohammedzaheeruddin.sampletest.retrofit;

import mohammedzaheeruddin.sampletest.entity.ApiData;
import mohammedzaheeruddin.sampletest.entity.PostData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */
public interface APIService {

    @POST("json.php?action=newCmsPlacesInterview")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ApiData> Request(@Body PostData id);
}
