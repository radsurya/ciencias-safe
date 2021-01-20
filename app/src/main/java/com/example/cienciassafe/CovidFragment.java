package com.example.cienciassafe;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CovidFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CovidFragment extends Fragment {
    private View v = null;
    String[] order = null;
    //public WebView mWebView;

    public CovidFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CovidFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CovidFragment newInstance(String param1, String param2) {
        CovidFragment fragment = new CovidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_covid, container, false);
        order = getActivity().getResources().getStringArray(R.array.order);

        if (v != null) {

            AutoCompleteTextView dropdown = v.findViewById(R.id.menu_order);

            if (dropdown != null) {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, order);
                dropdown.setAdapter(arrayAdapter);
                dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        //Toast.makeText(getActivity(), "Selected option: " + buildings[position], Toast.LENGTH_SHORT).show();

                        DatabaseReference reff;
                        reff = FirebaseDatabase.getInstance().getReference();

                        reff.child("situation_report").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    System.out.println(dataSnapshot.getChildrenCount());

                                    ArrayList<String> weeks = new ArrayList<>();

                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        weeks.add(d.child("week").getValue(String.class) + ";" + d.child("confirmed_cases").getValue(String.class) + ";" + d.child("high_risk").getValue(String.class) + ";" + d.child("tests").getValue(String.class));
                                    }

                                    if (position == 1) {
                                        Collections.reverse(weeks);
                                    }

                                    RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
                                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                                    CovidReportRecyclerViewAdapter recyclerViewAdapter = new CovidReportRecyclerViewAdapter(weeks);
                                    recyclerView.setAdapter(recyclerViewAdapter);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                System.out.println("Failed to read value. " + error.toException());
                            }
                        });

                    }

                });

                dropdown.setText(dropdown.getAdapter().getItem(1).toString(), false);

                DatabaseReference reff;
                reff = FirebaseDatabase.getInstance().getReference();

                reff.child("situation_report").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            System.out.println(dataSnapshot.getChildrenCount());

                            ArrayList<String> weeks = new ArrayList<>();

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                weeks.add(d.child("week").getValue(String.class) + ";" + d.child("confirmed_cases").getValue(String.class) + ";" + d.child("high_risk").getValue(String.class) + ";" + d.child("tests").getValue(String.class));
                            }

                            Collections.reverse(weeks);

                            RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                            CovidReportRecyclerViewAdapter recyclerViewAdapter = new CovidReportRecyclerViewAdapter(weeks);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            TypedValue tv = new TypedValue();
                            int actionBarHeight = 0;
                            if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics()) + 100;
                            }

                            recyclerView.setPadding(0, 0, 0, actionBarHeight);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        System.out.println("Failed to read value. " + error.toException());
                    }
                });

            }

        }

        // Inflate the layout for this fragment
        return v;
    }
}