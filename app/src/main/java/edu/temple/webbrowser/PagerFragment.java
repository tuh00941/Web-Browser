package edu.temple.webbrowser;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFragment extends Fragment {

    ViewPager viewPager;
    ArrayList<PageViewerFragment> pageList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagerFragment newInstance(String param1, String param2) {
        PagerFragment fragment = new PagerFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View l = inflater.inflate(R.layout.fragment_pager, container, false);

        viewPager = l.findViewById(R.id.view_pager);

        pageList = new ArrayList<>();

        if(savedInstanceState != null) {
            int numPages = savedInstanceState.getInt("Number of Pages");
            if(numPages > 0) {
                System.out.println(numPages + " Pages");
                String page = "Page ";
                for(int i = 0; i < numPages; i++) {
                    pageList.add((PageViewerFragment) getChildFragmentManager().getFragment(savedInstanceState, page + i));
                }
            }
        }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return pageList.get(position);
            }

            @Override
            public int getCount() {
                return pageList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {}
        });

        if(savedInstanceState != null) {
            viewPager.setCurrentItem(savedInstanceState.getInt("Current Page"));
            System.out.println("Page Number " + (viewPager.getCurrentItem() + 1));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((PageViewerFragment.SetUrlInterface) getActivity()).setUrl(pageList.get(position).link);

                String title = pageList.get(position).webView.getTitle();
                if(!(title == null || title.length() == 0)) {
                    getActivity().setTitle(title);
                    ((PageListFragment.PageListInterface) getActivity()).updateTitle(position, title);
                }
                else if(!(pageList.get(position).webView.getUrl() == null || pageList.get(position).webView.getUrl().length() == 0)) {
                    getActivity().setTitle(pageList.get(position).webView.getUrl());
                    ((PageListFragment.PageListInterface) getActivity()).updateTitle(position, pageList.get(position).webView.getUrl());
                }
                else {
                    getActivity().setTitle("New Tab");
                    ((PageListFragment.PageListInterface) getActivity()).updateTitle(position, "New Tab");
                }
            }

            @Override
            public void onPageSelected(int position) {
                ((PageViewerFragment.SetUrlInterface) getActivity()).setUrl(pageList.get(position).link);

                String title = pageList.get(position).webView.getTitle();
                if(!(title == null || title.length() == 0)) {
                    getActivity().setTitle(title);
                    ((PageListFragment.PageListInterface) getActivity()).updateTitle(position, title);
                }
                else if(!(pageList.get(position).webView.getUrl() == null || pageList.get(position).webView.getUrl().length() == 0)) {
                    getActivity().setTitle(pageList.get(position).webView.getUrl());
                    ((PageListFragment.PageListInterface) getActivity()).updateTitle(position, pageList.get(position).webView.getUrl());
                }
                else {
                    getActivity().setTitle("New Tab");
                    ((PageListFragment.PageListInterface) getActivity()).updateTitle(position, "New Tab");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(savedInstanceState != null) {
            System.out.println("Number of pages: " + pageList.size());
            System.out.println(viewPager != null);
        }
        else {
            ((BrowserControlFragment.BrowserControlInterface) getActivity()).makePage();
        }

        return l;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Current Page", viewPager.getCurrentItem());
        outState.putInt("Number of Pages", pageList.size());

        String page = "Page ";
        int i = 0;

        for(PageViewerFragment pageViewerFragment : pageList) {
            getChildFragmentManager().putFragment(outState, page + i, pageViewerFragment);
            i++;
        }
    }
}