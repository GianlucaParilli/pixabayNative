package gluka.pixabay;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Gluka on 10/14/17.
 */

public interface PixabayConnection {
    String BASE_URL ="https://pixabay.com/";
    String PATH = "api/?key=6618667-ce8759bc156aca8863c98ae6d&editors_choice";
    //String QUERY = "?base=USD&symbols=CAD";
    String FEED = BASE_URL+ PATH; //+ QUERY

    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
            addConverterFactory(GsonConverterFactory.create()).build();

    @GET(FEED)
    Call<PixResults> getResponse(
            @QueryMap Map<String, String> options
    );

}
