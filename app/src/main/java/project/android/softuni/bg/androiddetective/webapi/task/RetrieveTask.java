package project.android.softuni.bg.androiddetective.webapi.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;

/**
 * Created by Milko on 22.9.2016 г..
 */

public class RetrieveTask extends AsyncTask <Void, Void, String>{
  private static final String API_URL = Constants.WEB_API_URL;
  private static final String API_KEY = "27a4af35bdff5736";
  private static final String API_EMAIL = "bart@fullcontact.com";
  private Exception exception;

  protected void onPreExecute() {
//    progressBar.setVisibility(View.VISIBLE);
//    responseView.setText("");
  }

  protected String doInBackground(Void... urls) {
    // Do some validation here
    String blobId = "";

    try {
      URL url = new URL(API_URL + "/" + blobId);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
      }
      finally{
        urlConnection.disconnect();
      }
    }
    catch(Exception e) {
      Log.e("ERROR", e.getMessage(), e);
      return null;
    }
  }

  protected void onPostExecute(String response) {
    if(response == null) {
      response = "THERE WAS AN ERROR";
    }
    //progressBar.setVisibility(View.GONE);
    Log.i("INFO", response);
    ResponseBase.getDataMap().putAll(GsonManager.convertGsonStringToObjectMap(response));
    //responseView.setText(response);
    // TODO: check this.exception
    // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
  }
}
