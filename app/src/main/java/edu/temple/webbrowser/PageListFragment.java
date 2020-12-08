package edu.temple.webbrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageListFragment extends Fragment {

    public PageListInterface parentActivity;
    public ListView pageList;
    public ArrayList<String> titleList;
    public ArrayAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof PageListInterface) {
            parentActivity = (PageListInterface) context;
        }
        else {
            throw new RuntimeException("Implement the PageListInterface. Please.");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PageListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageListFragment newInstance(String param1, String param2) {
        PageListFragment fragment = new PageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            titleList = savedInstanceState.getStringArrayList("Title List");
        }
        else {
            titleList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View l = inflater.inflate(R.layout.fragment_page_list, container, false);

        pageList = l.findViewById(R.id.page_list);

        adapter = new ArrayAdapter(this.getActivity(), R.layout.textview_title, R.id.textView, titleList);

        pageList.setAdapter(adapter);

        pageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parentActivity.switchTab(position);
            }
        });

        return l;
    }

    public void updateTitle(int index, String title) {
        titleList.set(index, title);
        adapter.notifyDataSetChanged();
        /*TextView textView = (TextView) pageList.getChildAt(index);
        textView.setText(title);*/
    }

    public void addView(String title) {
        titleList.add(title);
        adapter.notifyDataSetChanged();
        /*TextView textView = new TextView(getActivity());
        textView.setText(title);*/

        //pageList.addView(textView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("Title List", titleList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //((BrowserActivity) parentActivity).handleIntent(4);
    }

    interface PageListInterface {
        void updateTitle(int index, String title);
        void addView(String title);
        void switchTab(int index);
    }
}