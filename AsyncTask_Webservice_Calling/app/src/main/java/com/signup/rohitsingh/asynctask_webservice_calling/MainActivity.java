package com.signup.rohitsingh.asynctask_webservice_calling;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    Button mBtn;
    TextView mTextView;
    ProgressBar mProgressbar;

    String HTTP_URL = "http://pjassociates.in/testphp/upload.php";
    String HTTP_URL1 = "//http://pjassociates.in/myweb/webservice.asmx";

    //http://pjassociates.in/myweb/webservice.asmx


    List<MyTask> lists;

    List<FlowerDataJsonModel> flowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button) findViewById(R.id.main_btn);
        mTextView = (TextView) findViewById(R.id.main_textView);
        mProgressbar = (ProgressBar) findViewById(R.id.main_progress);
        mProgressbar.setVisibility(View.INVISIBLE);

        lists = new ArrayList<MyTask>();
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Code for testing the AsyncTask Functinality Only
                //  testAsysncTask();

                // Code for calling GET request from service
                callGetRequest();


            }
        });

    }

    protected void callGetRequest () {

        MyTask task = new MyTask();

        // Uncomment the folliwing code for serial processing
        // task.execute("Param1","Param 2","Param 3");

        // Uncomment the following code for parallel processing
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, HTTP_URL1);
    }

    protected void testAsysncTask () {

        MyTask task = new MyTask();

        // Uncomment the folliwing code for serial processing
        // task.execute("Param1","Param 2","Param 3");

        // Uncomment the following code for parallel processing
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, HTTP_URL1, "Param 2", "Param 3");
    }

    public void updateTextViewWithString (String str) {

        mTextView.append(str + "\n");

    }

    public void updateTextViewWithArrayList (List <FlowerDataJsonModel> flowerList) {


        for (int i = 0; i < flowerList.size(); i ++){

            FlowerDataJsonModel obj = flowerList.get(i);

            mTextView.append(obj.getCategory() + "\n");
            mTextView.append(String.valueOf(obj.getPrice()) + "\n");
            mTextView.append(String.valueOf(obj.getProductId()) + "\n");
            mTextView.append(obj.getPhoto() + "\n");
            mTextView.append(obj.getName() + "\n");



        }

    }


    private class MyTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateTextViewWithString("Processing started");

            // Uncomment this code for serial processing
           /*
            mProgressbar.setVisibility(View.VISIBLE);
            */

            // Uncomment this code for parallel processing

            if (lists.size() == 0){
                mProgressbar.setVisibility(View.VISIBLE);
            }
            lists.add(this);



        }

        @Override
        protected String doInBackground(String... params) {

            // For testing the Async task background procession
            // return testingAsycBackgroundProcession();


            String strResponse = HttpManager.getData(params[0]);
            return  strResponse;
        }

        protected String testingAsycBackgroundProcession (String...params) {
            for (int i = 0 ; i < params.length; i++){

                publishProgress("Working with" + params[i]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task completed" + "\n";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            flowers = FlowerJsonParser.getFlowerJson(s);


          //  updateTextViewWithArrayList(flowers);
            updateTextViewWithString(s);

            // Uncomment the following code for serial processing
            // mProgressbar.setVisibility(View.INVISIBLE);

            // Uncomment the following code for parallel processing
            lists.remove(this);
            if (lists.size() == 0)
            {
                mProgressbar.setVisibility(View.INVISIBLE);
            }


        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            updateTextViewWithString(values[0]);

        }
    }


}
