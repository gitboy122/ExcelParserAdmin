package pro.kondratev.xlsxpoiexample;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadExcel extends Activity implements View.OnClickListener {

    EditText e1,e2;
    Button b1,b2;
    String url,fName;
    TextView t1;
    Boolean b;

    private String LOG_TAG = "ReadExcelFromUrl";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_layout);
        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText2);
        b1=(Button)findViewById(R.id.button) ;
        b1.setOnClickListener(this);
        b2=(Button)findViewById(R.id.listtoremove) ;
        b2.setOnClickListener(this);
        t1=(TextView)findViewById(R.id.textView);
        //URL path for the Excel file
         //"https://firebasestorage.googleapis.com/v0/b/excel-fd9c9.appspot.com/o/sudhansh1.xls?alt=media&token=5dc3111a-03ca-4b7f-930d-594ecbebbc4f";
        t1.setText("Upload File Here");
        b=Boolean.FALSE;
    }


    public void excelURL(String url) {
        Log.v(LOG_TAG, url);
        new ExcelURL().execute(url);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button) {
            url =e1.getText().toString();
            String link = e1.getText().toString();
            String year = e2.getText().toString();
            if (TextUtils.isEmpty(link) || TextUtils.isEmpty(year)) {
                Toast.makeText(getApplicationContext(), "Enter required parameters", Toast.LENGTH_SHORT).show();
                return;
            }
            t1.setText("Uploading .....");
            fName = String.valueOf(e2.getText());
            excelURL(url);
        }
        if(v.getId()==R.id.listtoremove) {
            Intent intent = new Intent(ReadExcel.this,Main3Activity.class);
            startActivity(intent);

        }
        }



    private class ExcelURL extends AsyncTask<String, Void, String> {
        private static final int REGISTRATION_TIMEOUT = 3 * 1000;
        private static final int WAIT_TIMEOUT = 30 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private ProgressDialog dialog = new ProgressDialog(ReadExcel.this);

        protected void onPreExecute() {
            dialog.setMessage("Getting your data... Please wait...");
            dialog.show();
        }

        protected String doInBackground(String... urls) {

            String URL = null;

            try {

                URL = urls[0];
                HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
                ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

                HttpGet httpGet = new HttpGet(URL);
                response = httpclient.execute(httpGet);

                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    b=Boolean.TRUE;
                    read r=new read(response.getEntity().getContent());
                    r.readExcel(fName);
                } else {
                    //Closes the connection.
                    Log.w("HTTP1:", statusLine.getReasonPhrase());
                    response.getEntity().getContent().close();

                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                b=Boolean.FALSE;

                Log.w("HTTP2:", e);
                content = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                b=Boolean.FALSE;

                Log.w("HTTP3:", e);
                content = e.getMessage();
                cancel(true);
            } catch (Exception e) {
                b=Boolean.FALSE;

                Log.w("HTTP4:", e);
                content = e.getMessage();
                cancel(true);
            }

            return content;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(ReadExcel.this,
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            if(b==Boolean.TRUE)
                t1.setText("Sucessfully Uploaded !");
            else
                t1.setText("Check your link unsuccessful");

        }

        protected void onPostExecute(String content) {
            dialog.dismiss();
            if(b==Boolean.TRUE)
            t1.setText("Sucessfully Uploaded !");
            else
                t1.setText("Check your connection unsuccessful");

        }


        private void parseExcel(InputStream fis) {


        }
    }
}