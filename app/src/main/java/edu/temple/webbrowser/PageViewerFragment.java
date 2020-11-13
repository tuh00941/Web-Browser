package edu.temple.webbrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageViewerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SetUrlInterface parentActivity;
    public View l;
    public WebView webView;
    public String link;

    public int position;
    public String pageTitle;

    public PageViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof SetUrlInterface) {
            parentActivity = (SetUrlInterface) context;
        }
        else {
            throw new RuntimeException("Implement the SetUrlInterface. Please.");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PageViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageViewerFragment newInstance(String param1, String param2) {
        PageViewerFragment fragment = new PageViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        l = inflater.inflate(R.layout.fragment_page_viewer, container, false);
        webView = l.findViewById(R.id.web_view);
        MyWebViewClient webViewClient = new MyWebViewClient();
        webView.setWebViewClient(webViewClient);
        MyOtherWebViewClient myOtherWebViewClient = new MyOtherWebViewClient();
        webView.setWebChromeClient(myOtherWebViewClient);
        webView.getSettings().setJavaScriptEnabled(true);

        if(savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
            System.out.println("Restoring web view");
            position = savedInstanceState.getInt("Position");
        }
        else {
            ((PageListFragment.PageListInterface) ((PagerFragment) getParentFragment()).getActivity()).addView(webView.getTitle());
        }

        return l;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        webView.saveState(outState);
        outState.putInt("Position", position);
    }

    private class MyWebViewClient extends WebViewClient {

        private MyWebViewClient() {
            super();
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            link = url;
            System.out.println("Position = " + position);
            if(position == ((PagerFragment) getParentFragment()).viewPager.getCurrentItem()) {
                ((SetUrlInterface) getParentFragment().getActivity()).setUrl(url);
                if(!(view.getTitle() == null || view.getTitle().length() == 0)) {
                    pageTitle = view.getTitle();
                    getParentFragment().getActivity().setTitle(view.getTitle());
                    ((PageListFragment.PageListInterface) getParentFragment().getActivity()).updateTitle(position, view.getTitle());
                }
                else if(!(url == null || url.length() == 0)) {
                    pageTitle = url;
                    getParentFragment().getActivity().setTitle(pageTitle);
                    ((PageListFragment.PageListInterface) getParentFragment().getActivity()).updateTitle(position, pageTitle);
                }
                else {
                    pageTitle = "New Tab";
                    getParentFragment().getActivity().setTitle(pageTitle);
                    ((PageListFragment.PageListInterface) getParentFragment().getActivity()).updateTitle(position, pageTitle);
                }

                System.out.println("Viewer-Made Title = <" + getParentFragment().getActivity().getTitle() + ">");
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(getParentFragment().getActivity() != null && position == ((PagerFragment) getParentFragment()).viewPager.getCurrentItem()) {
                pageTitle = view.getTitle();
                getParentFragment().getActivity().setTitle(view.getTitle());
                ((PageListFragment.PageListInterface) getParentFragment().getActivity()).updateTitle(position, view.getTitle());
                System.out.println("Client-Made Title = <" + getParentFragment().getActivity().getTitle() + ">");
            }
        }
    }

    private class MyOtherWebViewClient extends WebChromeClient {

        private MyOtherWebViewClient() { super(); }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if(position == ((PagerFragment) getParentFragment()).viewPager.getCurrentItem()) {
                if(!(title == null || title.length() == 0)) {
                    pageTitle = title;
                    if(getParentFragment().getActivity() != null) {
                        getParentFragment().getActivity().setTitle(title);
                        ((PageListFragment.PageListInterface) getParentFragment().getActivity()).updateTitle(position, title);
                    }
                }
            }
            /*if(!(title == null || title.length() == 0)) {
                pageTitle = title;
                if((PageListFragment.PageListInterface) getParentFragment().getActivity() != null) {
                    ((PageListFragment.PageListInterface) getParentFragment().getActivity()).updateTitle(position, title);
                }
            }*/
        }
    }

    interface SetUrlInterface {
        void setUrl(String url);
    }
}