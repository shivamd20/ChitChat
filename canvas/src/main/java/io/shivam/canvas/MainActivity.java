package io.shivam.canvas;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the widgets reference from XML layout
        final WebView wv = (WebView) findViewById(R.id.wv);

        wv.setWebViewClient(new WebViewClient());

                /*
                    WebSettings
                        Manages settings state for a WebView. When a
                        WebView is first created, it obtains a set
                        of default settings.

                    setJavaScriptEnabled(boolean flag)
                        Tells the WebView to enable JavaScript execution.
                 */
        wv.getSettings().setJavaScriptEnabled(true);

        // Get the Android assets folder path
        String folderPath = "file:///android_asset/fabric/";

        // Get the HTML file name
        String fileName = "test.html";

        // Get the exact file location
        String file = folderPath + fileName;

                /*
                    loadUrl(String url)
                        Loads the given URL.
                 */

        // Render the HTML file on WebView
        wv.loadUrl(file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wv.evaluateJavascript("alert('raamesh')", null);
        }


        wv.setBackgroundColor(Color.RED);

        wv.addJavascriptInterface(new WebAppInterface(this), "Android");




    }
}
