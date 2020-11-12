package edu.temple.webbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.webkit.WebView;

public class BrowserActivity extends AppCompatActivity implements PageControlFragment.LoadPageInterface, PageViewerFragment.SetUrlInterface {

    PageViewerFragment pageViewerFragment;
    PageControlFragment pageControlFragment;

    Fragment tmpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        if ((tmpFragment = fm.findFragmentById(R.id.page_control)) instanceof PageControlFragment)
            pageControlFragment = (PageControlFragment) tmpFragment;
        else {
            pageControlFragment = new PageControlFragment();
            fm.beginTransaction()
                    .add(R.id.page_control, pageControlFragment)
                    .commit();
        }

        if ((tmpFragment = fm.findFragmentById(R.id.page_viewer)) instanceof PageViewerFragment)
            pageViewerFragment = (PageViewerFragment) tmpFragment;
        else {
            pageViewerFragment = new PageViewerFragment();
            fm.beginTransaction()
                    .add(R.id.page_viewer, pageViewerFragment)
                    .commit();
        }
    }

    @Override
    public void loadPage(String url) {
        if(url.substring(0, 8).equals("https://") || url.substring(0, 7).equals("http://")) {
            System.out.println(url);
            pageViewerFragment.webView.loadUrl(url);
        }
        else {
            String editedUrl = "http://" + url;
            System.out.println(editedUrl);
            pageViewerFragment.webView.loadUrl(editedUrl);
        }
    }

    @Override
    public void goBack() {
        if(pageViewerFragment.webView.canGoBack()) {
            pageViewerFragment.webView.goBack();
        }
    }

    @Override
    public void goForward() {
        if(pageViewerFragment.webView.canGoForward()) {
            pageViewerFragment.webView.goForward();
        }
    }

    @Override
    public void setUrl(String url) {
        pageControlFragment.urlView.setText(url);
    }
}