package com.example.cienciassafe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResourceReportHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResourceReportHistory extends Fragment {

    private View v = null;

    public ResourceReportHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResourceReportHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static ResourceReportHistory newInstance() {
        ResourceReportHistory fragment = new ResourceReportHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = (View) inflater.inflate(R.layout.fragment_resource_report_history, container, false);

        if (v != null) {

            TextView teste = v.findViewById(R.id.teste);
            DatabaseReference reff;
            reff = FirebaseDatabase.getInstance().getReference();

            reff.child("resourceReport").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        ArrayList<String> classrooms = new ArrayList<>();

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            classrooms.add(getActivity().getResources().getString(R.string.room) + " " + d.getKey().replace("-", ".") + ";" + d.child("occupation").getValue(String.class));
                            System.out.println(d.getValue());
                        }

//                        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
//                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
//                        ClassroomRecyclerViewAdapter recyclerViewAdapter = new ClassroomRecyclerViewAdapter(classrooms);
//                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    System.out.println("Failed to read value. " + error.toException());
                }
            });


        }
        return v;
    }
}