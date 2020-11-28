package edu.temple.webbrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class BrowserActivity extends AppCompatActivity implements PageControlFragment.LoadPageInterface, PageViewerFragment.SetUrlInterface, BrowserControlFragment.BrowserControlInterface, PageListFragment.PageListInterface {

    PagerFragment pagerFragment;
    //PageViewerFragment pageViewerFragment;
    PageControlFragment pageControlFragment;
    BrowserControlFragment browserControlFragment;
    PageListFragment pageListFragment;

    Fragment tmpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        /*if (savedInstanceState != null && findViewById(R.id.page_list) != null)
            pageListFragment = (PageListFragment) fm.getFragment(savedInstanceState, "Page List");
        else {
            pageListFragment = new PageListFragment();
        }
        if(findViewById(R.id.page_list) != null) {
            fm.beginTransaction()
                    .replace(R.id.page_list, pageListFragment)
                    .commit();
        }*/

        if (savedInstanceState != null)
            pageListFragment = (PageListFragment) fm.getFragment(savedInstanceState, "Page List");
        else if ((tmpFragment = fm.findFragmentById(R.id.page_list)) instanceof PageListFragment)
            pageListFragment = (PageListFragment) tmpFragment;
        else {
            pageListFragment = new PageListFragment();
            fm.beginTransaction()
                    .replace(R.id.page_list, pageListFragment)
                    .commit();
        }

        if ((tmpFragment = fm.findFragmentById(R.id.page_control)) instanceof PageControlFragment)
            pageControlFragment = (PageControlFragment) tmpFragment;
        else {
            pageControlFragment = new PageControlFragment();
            fm.beginTransaction()
                    .add(R.id.page_control, pageControlFragment)
                    .commit();
        }

        if ((tmpFragment = fm.findFragmentById(R.id.page_viewer)) instanceof PagerFragment)
            pagerFragment = (PagerFragment) tmpFragment;
        else {
            System.out.println("Pager created.");
            pagerFragment = new PagerFragment();
            fm.beginTransaction()
                    .add(R.id.page_viewer, pagerFragment)
                    .commit();
        }

        if ((tmpFragment = fm.findFragmentById(R.id.browser_control)) instanceof BrowserControlFragment)
            browserControlFragment = (BrowserControlFragment) tmpFragment;
        else {
            browserControlFragment = new BrowserControlFragment();
            fm.beginTransaction()
                    .add(R.id.browser_control, browserControlFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(findViewById(R.id.page_list) != null) {
            getSupportFragmentManager().putFragment(outState, "Page List", pageListFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
            String url = sharedPreferences.getString("url" + data.getIntExtra("position", 0), null);

            pageControlFragment.urlView.setText(url);
            pageControlFragment.url = url;

            loadPage(url);
        }
    }

    @Override
    public void loadPage(String url) {
        if(pagerFragment.pageList.size() > 0) {
            PageViewerFragment page = pagerFragment.pageList.get(pagerFragment.viewPager.getCurrentItem());

            if(url.length() >= 7 && (url.substring(0, 8).equals("https://") || url.substring(0, 7).equals("http://"))) {
                System.out.println(url);
                page.webView.loadUrl(url);
            }
            else {
                String editedUrl = "http://" + url;
                System.out.println(editedUrl);
                page.webView.loadUrl(editedUrl);
            }

            String title = page.webView.getTitle();
            if(!(title == null || title.length() == 0)) {
                this.setTitle(title);
                updateTitle(page.position, title);
            }
            else if(!(url == null || url.length() == 0)) {
                this.setTitle(url);
                updateTitle(page.position, url);
            }
            else {
                this.setTitle("New Tab");
                updateTitle(page.position, "New Tab");
            }

            System.out.println("Browser-Made Title = <" + this.getTitle() + ">");

            page.link = url;
        }
    }

    @Override
    public void goBack() {
        if(pagerFragment.pageList.size() > 0) {
            if(pagerFragment.pageList.get(pagerFragment.viewPager.getCurrentItem()).webView.canGoBack()) {
                pagerFragment.pageList.get(pagerFragment.viewPager.getCurrentItem()).webView.goBack();
            }
        }
    }

    @Override
    public void goForward() {
        if(pagerFragment.pageList.size() > 0) {
            if(pagerFragment.pageList.get(pagerFragment.viewPager.getCurrentItem()).webView.canGoForward()) {
                pagerFragment.pageList.get(pagerFragment.viewPager.getCurrentItem()).webView.goForward();
            }
        }
    }

    @Override
    public void setUrl(String url) {
        pageControlFragment.urlView.setText(url);
    }

    @Override
    public void makePage() {
        PageViewerFragment newPage = new PageViewerFragment();
        newPage.position = pagerFragment.pageList.size();
        pagerFragment.pageList.add(newPage);
        pagerFragment.viewPager.getAdapter().notifyDataSetChanged();
        pagerFragment.viewPager.setCurrentItem(pagerFragment.pageList.size() - 1, true);
        System.out.println("Number of pages: " + pagerFragment.pageList.size());
    }

    @Override
    public void saveBookmark() {
        SharedPreferences sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int bookmarkCount = sharedPreferences.getInt("bookmarkCount", 0);

        if(pagerFragment.pageList.size() > 0) {
            PageViewerFragment page = pagerFragment.pageList.get(pagerFragment.viewPager.getCurrentItem());
            String url = page.link;

            if(url != null) {
                boolean containsUrl = false;
                for (int i = 1; i <= bookmarkCount; i++) {
                    if (sharedPreferences.getString("url" + i, null).equals(url)) {
                        containsUrl = true;
                        break;
                    }
                }

                if (!containsUrl) {
                    bookmarkCount++;
                    editor.putInt("bookmarkCount", bookmarkCount).commit();
                    editor.putString("url" + bookmarkCount, url).commit();
                    editor.putString("title" + bookmarkCount, page.pageTitle).commit();
                    Toast.makeText(BrowserActivity.this, "Bookmark saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void viewBookmarks() {
        Intent bookmarksActivityIntent = new Intent(BrowserActivity.this, BookmarksActivity.class);
        //bookmarksActivityIntent.putExtra("data", data);
        startActivityForResult(bookmarksActivityIntent, 1);
    }

    @Override
    public void updateTitle(int index, String title) {
        pageListFragment.updateTitle(index, title);
    }

    @Override
    public void addView(String title) {
        pageListFragment.addView(title);
    }

    @Override
    public void switchTab(int index) {
        pagerFragment.viewPager.getAdapter().notifyDataSetChanged();
        pagerFragment.viewPager.setCurrentItem(index, true);
    }
}