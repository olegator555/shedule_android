package com.olegator555.rasp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link First_launch_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class First_launch_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView textView;
    private AutoCompleteTextView autoCompleteTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private ArrayList<String> mParam2;

    public First_launch_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment First_launch_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static First_launch_fragment newInstance(String param1, ArrayList<String> param2) {
        First_launch_fragment fragment = new First_launch_fragment();
        Bundle args = new Bundle();
        args.putString(First_launch.HEADER_KEY, param1);
        args.putStringArrayList(First_launch.DATA_LIST_KEY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(First_launch.HEADER_KEY, "Null");
            mParam2 = getArguments().getStringArrayList(First_launch.DATA_LIST_KEY);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_launch_fragment, container, false);
        textView = view.findViewById(R.id.fragmentHeader);
        autoCompleteTextView = view.findViewById(R.id.fragmentAutocomplete);
        textView.setText(mParam1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                mParam2);
        autoCompleteTextView.setAdapter(adapter);
        return view;
    }
}