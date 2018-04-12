package gluka.pixabay;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    static String result;
    ArrayList<Hit> hitsArray = new ArrayList<>();
    ImageView picture;
    int totalClicks = 0;
    private TextToSpeech tts;
    String tag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //final TextToSpeech tts = null;
        picture = (ImageView) findViewById(R.id.pictureIV);

        Log.d("print", "onClick: " + "Loading Please Wait");
        new FetchAsyncTask().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        tts.speak(tag, TextToSpeech.QUEUE_FLUSH, null, null);

                    }
                });

                //tts.setLanguage(Locale.US);
                //tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);
                Picasso.with(getApplicationContext()).load(hitsArray.get(totalClicks).getWebformatURL()).fit().into(picture);
                tag = hitsArray.get(totalClicks).getTags();
                Snackbar.make(view, "Tags: " + tag, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                totalClicks++;
                if(totalClicks==19){
                    totalClicks=0;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(getApplicationContext(),"Clicked about button", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class FetchAsyncTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void...params) {

            PixabayConnection conn = PixabayConnection.retrofit.create(PixabayConnection.class);
            Map<String,String> data = new HashMap<>();
            Call<PixResults> call = conn.getResponse(data);
            PixResults response;

            try {
                response = call.execute().body();
                for(Hit hits : response.getHits()){
                    hitsArray.add(hits);
                    //Log.d("array print", "doInBackground: " + urlArray.add(hits.getWebformatURL()));
                }
                String URLString = response.getHits().get(0).getWebformatURL();
                result = URLString;


            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        };

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result = s;
        }
    }
}



