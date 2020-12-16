package com.example.cienciassafe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassroomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String[] buildings = null;
    private View v = null;
    public ClassroomsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        buildings = getActivity().getResources().getStringArray(R.array.list_buildings);
        v = (View) inflater.inflate(R.layout.fragment_classrooms, container, false);

        if (v != null) {
            Spinner spin = (Spinner) v.findViewById(R.id.spinner_room);
            if (spin != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, buildings);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                spin.setOnItemSelectedListener(this);
            }
        }

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getActivity(), "Selected option: "+buildings[position] , Toast.LENGTH_SHORT).show();

        final LinearLayout ll = (LinearLayout) v.findViewById(R.id.rooms);
        ll.removeAllViews();

        DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();



        reff.child("rooms").child(buildings[position].toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int i = 0;
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        TextView Room = new TextView(getActivity());
                        Room.setText(getActivity().getResources().getString(R.string.room)+" "+d.getKey().replace("-","."));
                        Room.setId(i);
                        Room.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                        Room.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        ll.addView(Room);
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value. " + error.toException());
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}