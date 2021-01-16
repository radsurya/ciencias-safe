package com.example.cienciassafe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassroomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomsFragment extends Fragment {
    private View v = null;
    String[] buildings = null;

    public ClassroomsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassroomsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassroomsFragment newInstance() {
        ClassroomsFragment fragment = new ClassroomsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = (View) inflater.inflate(R.layout.fragment_classrooms, container, false);
        buildings = getActivity().getResources().getStringArray(R.array.list_buildings);

        if (v != null) {

            AutoCompleteTextView dropdown = v.findViewById(R.id.menu_rooms);

            if (dropdown != null) {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, buildings);
                dropdown.setAdapter(arrayAdapter);
                dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), "Selected option: " + buildings[position], Toast.LENGTH_SHORT).show();

                        DatabaseReference reff;
                        reff = FirebaseDatabase.getInstance().getReference();

                        reff.child("rooms").child(buildings[position].toLowerCase()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    ArrayList<String> classrooms = new ArrayList<>();

                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        classrooms.add(getActivity().getResources().getString(R.string.room) + " " + d.getKey().replace("-", ".") + ";" + d.child("occupation").getValue(String.class));
                                    }

                                    RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
                                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                                    ClassroomRecyclerViewAdapter recyclerViewAdapter = new ClassroomRecyclerViewAdapter(classrooms);
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
            }
        }

        // Inflate the layout for this fragment
        return v;
    }

}