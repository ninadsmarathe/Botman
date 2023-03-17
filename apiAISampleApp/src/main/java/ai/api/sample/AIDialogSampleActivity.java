package ai.api.sample;

/***********************************************************************************************************************
 *
 * API.AI Android SDK -  API.AI libraries usage example
 * =================================================
 *
 * Copyright (C) 2015 by Speaktoit, Inc. (https://www.speaktoit.com)
 * https://www.api.ai
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************/

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIDialog;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import java.io.InputStream;
import java.util.List;



public class AIDialogSampleActivity extends AppCompatActivity implements AIDialog.AIDialogListener,
        TextToSpeech.OnInitListener {

    private static final String TAG = AIDialogSampleActivity.class.getName();
    private TextView resultTextView;
    private AIDialog aiDialog;
    private String intentName;
    private String parameters;
    private String query;
    private TextToSpeech tts;
    private String speech;
    private String displayText;
    private String imageURL;
    private String websiteURL;
    private String location;
    private String question;
    private RecyclerView vertical_recycler_view,horizontal_recycler_view;
    private ArrayList<Data> horizontalList,verticalList;
    private HorizontalAdapter horizontalAdapter;
    private String response_from_api;
    private String speech_from_api;
    private EditText chatText;
    private ImageButton send,back;
    private static final String TAG1 = "ChatActivity";
    private boolean side = false;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    ArrayList<Images> imageList;
    private View view,footerView;
    ImageAdapter adapter;
    //public static final String URL_GET_REPLY = "http://192.168.0.101/api.php?intentName=";
   // public static final String URL_GET_CURL_REPLY = "http://192.168.0.101/curl.php?query=";


     public static final String URL_GET_REPLY = "http://rachitkaproject.esy.es/ghj.php?intentName=";
   public static final String URL_GET_CURL_REPLY = "http://rachitkaproject.esy.es/curl.php?query=";

//   public static final String URL_GET_REPLY = "http://dev.medlifepharma.com/ghj.php?intentName=";
//    public static final String URL_GET_CURL_REPLY = "http://dev.medlifepharma.com/curl.php?query=";


    private Gson gson = GsonFactory.getGson();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidialog_sample);
        tts = new TextToSpeech(this, this);
        getWindow().setBackgroundDrawableResource(R.drawable.walp);
        //     resultTextView = (TextView) findViewById(R.id.resultTextView);
        listView = (ListView) findViewById(R.id.listView1);
        //code to set adapter to populate list
        footerView =  ((LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        listView.addFooterView(footerView);
         //   image_listView=(ListView) findViewById(R.id.images);
          //  image_listView.setVisibility(View.GONE);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDialog = new AIDialog(this, config);
        aiDialog.setResultsListener(this);

        chatText=(EditText)findViewById(R.id.chatText);
        send=(ImageButton)findViewById(R.id.buttonSend);
        //progressBar=(ProgressBar)findViewById(R.id.firstBar);
        //      progressBar1=(ProgressBar)findViewById(R.id.spin_kit);
        //  mic=(Button)findViewById(R.id.buttonListen);




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chatText.getText().equals(""))
                {
                    question=chatText.getText().toString();
                    query=question;
                    sendChatMessage(question);

                    question=question.replaceAll(" ", "%20");
                    chatText.setText("");
                    listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    listView.setAdapter(chatArrayAdapter);

                    //to scroll the list view to bottom on data change
                    /*chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                        }
                    });*/
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                    getCurlResponse();
                }
            }
        });
    }

    @Override
    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                // parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                parameterString =entry.getKey() ;
            }
        }
        intentName=result.getMetadata().getIntentName();
        parameters=parameterString;
        question=result.getResolvedQuery();
        query=result.getResolvedQuery();


        //          Show results in TextView.
        //          resultTextView.setText("Query:" + result.getResolvedQuery() +
        //          "\nIntent: " + result.getMetadata().getIntentName() +
        //          "\nAction: " + result.getAction() +
        //          "\nParameters: " + parameterString);

        //          query="Query:" + result.getResolvedQuery() +
        //          "\nIntent: " + result.getMetadata().getIntentName() +
        //          "\nAction: " + result.getAction() +
        //          "\nParameters: " + parameterString;
        sendChatMessage(question);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        /*chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount()-1);
            }
        });*/

        listView.setSelection(chatArrayAdapter.getCount() - 1);
    /*    if(intentName=="Fallback")
         {
            Toast.makeText(AIDialogSampleActivity.this,"going",Toast.LENGTH_SHORT).show();
            receiveChatMessage(result.getFulfillment().getSpeech());
            speech="Sorry Can you Repeat the question please";
          }
    */
        getEmployee();

    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveChatMessage(error.toString());
            }
        });
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        if (aiDialog != null) {
            aiDialog.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (aiDialog != null) {
            aiDialog.resume();
        }
        super.onResume();
    }

    public void buttonListenOnClick(final View view) {
        aiDialog.showAndListen();
    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar.setVisibility(View.VISIBLE);
                //progressBar1.setVisibility(View.VISIBLE);
                footerView.setVisibility(View.VISIBLE);
                //loading = ProgressDialog.show(AIDialogSampleActivity.this,"Fetching...","Wait...",false,false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);
                //progressBar1.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);

                /// / loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(URL_GET_REPLY,intentName,parameters,question);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config1.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            speech = c.getString(Config1.TAG_SPEECH);
            displayText = c.getString(Config1.TAG_DISPLAY_TEXT);
            imageURL = c.getString(Config1.TAG_IMAGE_URL).toString();
            websiteURL = c.getString(Config1.TAG_WEBSITE_URL).toString();
            location = c.getString(Config1.TAG_LOCATION).toString();

            /*if (intentName.trim().equals("Fallback"))
            {
                Toast.makeText(getApplicationContext(),"going",Toast.LENGTH_SHORT).show();
                speech="Can you please repeat the question";
                receiveChatMessage("I dont Understand what you are saying");
                speakOut();
            }*/


            if (!websiteURL.equals("null"))
            {
                receiveChatMessage(displayText);
                speakOut();

                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(websiteURL));
                startActivity(intent);
            }else
            if (!location.equals("null"))
            {   receiveChatMessage(displayText);
                speakOut();

                String[] latlong =  location.split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);

                String label = "Vivekanand Education Society's Institute of Technology";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }else
            if (!imageURL.equals("null"))
            {
                receiveImageChatMessage(displayText);
                speakOut();
            }else
            {
                // Toast.makeText(AIDialogSampleActivity.this,"Speech:" + speech + "\nDisplatText: " + displayText + "\nImageURL: " + imageURL+ "\nwebsiteURL: " + websiteURL+"\nlocation:"+location,Toast.LENGTH_LONG).show();
                //resultTextView.setText(query+"\nSpeech:" + speech + "\nDisplatText: " + displayText + "\nImageURL: " + imageURL);
                receiveChatMessage(displayText);
                speakOut();
                intentName="";
                parameters="";

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {
        String text = speech;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private boolean sendChatMessage(String text){
        chatArrayAdapter.add(new ChatMessage(false, text,false));
        return true;
    }

    private boolean receiveChatMessage(String text){
        chatArrayAdapter.add(new ChatMessage(true, text,false));
        return true;
    }
    private boolean receiveImageChatMessage(String text){
        chatArrayAdapter.add(new ChatMessage(true, text,true));
        return true;
    }

    private void getCurlResponse(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar.setVisibility(View.VISIBLE);
                // progressBar1.setVisibility(View.VISIBLE);
                footerView.setVisibility(View.VISIBLE);

                //loading = ProgressDialog.show(AIDialogSampleActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                // progressBar.setVisibility(View.GONE);
                // progressBar1.setVisibility(View.GONE);
                footerView.setVisibility(View.GONE);
                showCurlResponse(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetCurlRequestParam(URL_GET_CURL_REPLY,question);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showCurlResponse(String json){
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONObject object = jsonObject.getJSONObject("result");
            question=object.getString("resolvedQuery");
            //JSONObject object1=object.getJSONObject("parameter");
            JSONObject object2 = object.getJSONObject("metadata");
            intentName=object2.getString("intentName");


            JSONObject object1=object.getJSONObject("parameters");


            //      parameter1=object1.getString("Address");
            //      parameter2=object1.getString("Contact_No");
            //      parameter3=object1.getString("FullForm");

            if (intentName.equals("Show"))
            {
                if(!((object1.getString("Website")).equals(""))){
                    parameters="Website";
                }else
                if(!((object1.getString("Location")).equals(""))){
                    parameters="Location";
                }else
                if(!((object1.getString("Image")).equals(""))){
                    parameters="Image";
                }

            }else
            if(intentName.equals("AboutCollege"))
            {
                if(!((object1.getString("Address")).equals(""))){
                    parameters="Address";
                }else
                if(!((object1.getString("Contact_No")).equals(""))){
                    parameters="Contact_No";
                }else
                if(!((object1.getString("FullForm")).equals(""))){
                    parameters="FullForm";
                }else
                if(!((object1.getString("Details")).equals(""))){
                    parameters="Details";
                }else
                if(!((object1.getString("Principal")).equals(""))){
                    parameters="Principal";
                }else
                if(!((object1.getString("VP")).equals(""))){
                    parameters="VP";
                }else
                if(!((object1.getString("Departments")).equals(""))){
                    parameters="Departments";
                }else
                if(!((object1.getString("Events")).equals(""))){
                    parameters="Events";
                }else
                if(!((object1.getString("SportsEvent")).equals(""))){
                    parameters="SportsEvent";
                }else
                if(!((object1.getString("CulturalEvent")).equals(""))){
                    parameters="CulturalEvent";
                }else
                if(!((object1.getString("TechnicalEvent")).equals(""))){
                    parameters="TechnicalEvent";
                }else
                if(!((object1.getString("Committee")).equals(""))){
                    parameters="Committee";
                }else
                if(!((object1.getString("Placements")).equals(""))){
                    parameters="Placements";
                }else
                if(!((object1.getString("Company")).equals(""))){
                    parameters="Company";
                }else
                if(!((object1.getString("Facility")).equals(""))){
                    parameters="Facility";
                }




            }else
            if(intentName.equals("HOD"))
            {
                if(!((object1.getString("INFT")).equals(""))){
                    parameters="INFT";
                }else
                if(!((object1.getString("CMPN")).equals(""))){
                    parameters="CMPN";
                }else
                if(!((object1.getString("INST")).equals(""))){
                    parameters="INST";
                }else
                if(!((object1.getString("EXTC")).equals(""))){
                    parameters="EXTC";
                }else
                if(!((object1.getString("ETRX")).equals(""))){
                    parameters="ETRX";
                }else
                if(!((object1.getString("StudentCouncil")).equals(""))){
                    parameters="StudentCouncil";
                }else
                if(!((object1.getString("TPC")).equals(""))){
                    parameters="TPC";
                }


            }else
            if(intentName.equals("TimeTable"))
            {
                if(!((object1.getString("D20")).equals(""))){
                    parameters="D20";
                }else
                if(!((object1.getString("D15")).equals(""))){
                    parameters="D15";
                }else
                if(!((object1.getString("D10")).equals(""))){
                    parameters="D10";
                }
            }else
            if(intentName.equals("NormalTalkings"))
            {
                if(!((object1.getString("AboutYou")).equals(""))){
                    parameters="AboutYou";
                }else
                if(!((object1.getString("Compliments")).equals(""))){
                    parameters="Compliments";
                }else
                if(!((object1.getString("Feel")).equals(""))){
                    parameters="Feel";
                }else
                if(!((object1.getString("Greetings")).equals(""))){
                    parameters="Greetings";
                }else
                if(!((object1.getString("Help")).equals(""))){
                    parameters="Help";
                }else
                if(!((object1.getString("Made")).equals(""))){
                    parameters="Made";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getEmployee();

        //      output.setText("intent name:"+intentName+"\n Query Resolved:"+question+"\n parameters:"+parameter);

    }

    public class Images {
        private String image;
        public Images() {
        }
        //Getters and setters
        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }
    }
    public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

        private TextView chatText;
        private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
        private LinearLayout singleMessageContainer;

        private Context context;

        @Override
        public void add(ChatMessage object) {
            chatMessageList.add(object);
            super.add(object);
        }


        public ChatArrayAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public int getCount() {
            return this.chatMessageList.size();
        }

        public ChatMessage getItem(int index) {
            return this.chatMessageList.get(index);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
            }
            //imageurl=ChatBubbleActivity.imageUrl;
            singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
            ChatMessage chatMessageObj = getItem(position);
            chatText = (TextView) row.findViewById(R.id.singleMessage);
            chatText.setText(chatMessageObj.message);
            chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.left : R.drawable.right);
            singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);


            horizontal_recycler_view= (RecyclerView) row.findViewById(R.id.horizontal_recycler_view);
            if ( chatMessageObj.imageShow ) // If True
            {

                if(intentName.equals("Show") && parameters.equals("Image")){
                    horizontalList=new ArrayList<>();
                    horizontalList.add(new Data("horizontal 1",R.drawable.first));
                    horizontalList.add(new Data("horizontal 2",R.drawable.two));
                    horizontalList.add(new Data("horizontal 3",R.drawable.three));
                    horizontalList.add(new Data("horizontal 4",R.drawable.four));
                    horizontalList.add(new Data("horizontal 3",R.drawable.five));
                    horizontalList.add(new Data("horizontal 4",R.drawable.six));
                }else
                if(intentName.equals("TimeTable") && parameters.equals("D20")){

                    horizontalList=new ArrayList<>();
                    horizontalList.add(new Data("horizontal 1",R.drawable.final_year));
                }else
                if(intentName.equals("TimeTable") && parameters.equals("D15")){

                    horizontalList=new ArrayList<>();
                    horizontalList.add(new Data("horizontal 1",R.drawable.third));
                }else
                if(intentName.equals("TimeTable") && parameters.equals("D20")){

                    horizontalList=new ArrayList<>();
                    horizontalList.add(new Data("horizontal 1",R.drawable.second));
                }else
                if(intentName.equals("AboutCollege") && parameters.equals("Placements")){

                    horizontalList=new ArrayList<>();
                    horizontalList.add(new Data("horizontal 1",R.drawable.tcp));
                }

                horizontalAdapter=new HorizontalAdapter(horizontalList);

                LinearLayoutManager horizontalLayoutManagaer
                        = new LinearLayoutManager(AIDialogSampleActivity.this, LinearLayoutManager.HORIZONTAL, false);
                horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);

                horizontal_recycler_view.setAdapter(horizontalAdapter);

                horizontal_recycler_view.setVisibility(View.VISIBLE);






            }
            else {
                horizontal_recycler_view.setVisibility(View.GONE);
            }
            return row;
        }

        public Bitmap decodeToBitmap(byte[] decodedByte) {
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }



    }
    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        List<Data> horizontalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtView;
            public ImageView imgView;

            public MyViewHolder(View view) {
                super(view);
                // txtView = (TextView) view.findViewById(R.id.txtView);
                imgView=(ImageView) view.findViewById(R.id.image);


            }
        }


        public HorizontalAdapter(List<Data> horizontalList) {
            this.horizontalList = horizontalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_single, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            // holder.txtView.setText(horizontalList.get(position).title);
            //  holder.txtView.setText(horizontalList.get(position).title);
            holder.imgView.setImageResource(horizontalList.get(position).imgURL);



           /* holder.txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AIDialogSampleActivity.this,holder.txtView.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }


        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }
            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }

    public class Data {
        public String title;
        public int imgURL;

        Data(String title, int imgURL) {
            this.title = title;
            this.imgURL = imgURL;
        }

    }


    public class Winner_View {

        private String image;

        public Winner_View() {
            // TODO Auto-generated constructor stub
        }

        public Winner_View( String image) {
            super();
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }
}
