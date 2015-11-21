package jeremyjwz.testproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Map2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        Button button = (Button)findViewById(R.id.buttonMap2);
        button.setOnClickListener(new GetInfoListener());
    }
    class GetInfoListener implements Button.OnClickListener{
        @Override
        public void onClick(View v){
//            Log.e("Error","Button clocked");
//            new GetDataFromServer().execute("http://192.168.199.164/server.php?action=checkpoints");
            new GetDataFromServer().execute("http://10.214.149.168:8888/server.php?action=checkpoints");
        }
    }
    private InputStream OpenHttpConnection(String urlString) throws IOException{
        InputStream in =null;
        int response =-1;
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        try{
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            response = httpURLConnection.getResponseCode();
            if(response==HttpURLConnection.HTTP_OK)
                in = httpURLConnection.getInputStream();
        }
        catch (Exception ex){
            Log.e("Networking",ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private void foo(JSONArray jsonArray) throws JSONException{
        JSONObject json;
        for (int i = 0; i < jsonArray.length(); ++i) {
            json = jsonArray.getJSONObject(i);
            json.getString("longitude");
        }
    }
    private class GetDataFromServer extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }
        @Override
        protected void onPostExecute(String result){
            Log.e("Error","downloading..:" + result);
            try {
                JSONObject json = new JSONObject(result);
                Log.e("Map2", ""+json.getBoolean("status"));
                if (json.getBoolean("status")) {
                    foo(json.getJSONArray("data"));
                } else {
                    Log.e("Error","status false");
                }
            } catch (JSONException e) {
                Log.e("Map2", e.getMessage());
            }
//            TextView textView = (TextView)findViewById(R.id.textViewMap2);
//            textView.setText(result);
        }
    }
    private String DownloadText(String URL){
        int BUFFER_SIZE=2000;
        InputStream in =null;
        try{
            in = OpenHttpConnection(URL);
        }
        catch (IOException ex){
            Log.e("Networking", ex.getLocalizedMessage());
            return "";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str="";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try{
            while((charRead=isr.read(inputBuffer))>0){
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str+=readString;
                inputBuffer=new char[BUFFER_SIZE];

            }
        }catch (IOException ex){
            Log.e("Networking", ex.getLocalizedMessage());
            return "";
        }
        return str;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map2, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
