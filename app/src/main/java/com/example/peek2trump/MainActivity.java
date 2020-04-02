package com.example.peek2trump;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textview1;
    WebView tw;
    StringBuffer tw_page;
    String tw_ps = "<html><head></head><body>get twitter page failed.<br>retry maybe ok.</body><html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview1 = (TextView)findViewById(R.id.id_refresh);
        tw = (WebView)findViewById(R.id.id_tw);

    }

    private class fetchtwd extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... url) {
            try {
//                Proxy px = new Proxy(java.net.Proxy.Type.HTTP,
//                        new InetSocketAddress("127.0.0.1", 1087));

                //URL u = new URL("https://www.baidu.com/");
                URL u = new URL("https://twitter.com/realDonaldTrump");
                HttpURLConnection conn = (HttpURLConnection) u.openConnection(); //
           //     conn.addRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
               // c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println("能访问");
                    InputStream in = conn.getInputStream();
                    InputStreamReader r = new InputStreamReader(in);
                    BufferedReader buffer = new BufferedReader(r);
                    tw_page = new StringBuffer();
                    String line = null;
                    while ((line = buffer.readLine()) != null) {
                        tw_page.append(line);
                        System.out.println(line);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "return selected string"; // in tw_cont
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // 只挑选推特条目
            String tagA="<div class=\"dir-ltr\" dir=\"ltr\">";

            String tagB="</div>";
            String left=tw_page.toString();
            int tagai=left.indexOf(tagA);
            int len = left.length();
            System.out.println(len);
            if (len>0)
                tw_ps="";
            while (tagai>=0) {
                System.out.println("tagia 大于等于 0");
                left = left.substring(tagai+tagA.length()+2);
                int tagbi = left.indexOf(tagB);
                if (tagbi>=0) {
                    String p1=left.substring(0, tagbi);
                    tw_ps += "<br>"+p1+"<br>";
                }
                tagai=left.indexOf(tagA);
            }
            tw.loadData(tw_ps, "text/html", "utf-8");
        }
    }

    public void onClick_refresh(View view)
    {
        if (true) {
            String cont = "<html><head></head><body>fetching twitter of Trump ...</body><html>";
            tw.loadData(cont, "text/html", "utf-8");
        }
        new fetchtwd().execute("not-used-url");
    }

}
