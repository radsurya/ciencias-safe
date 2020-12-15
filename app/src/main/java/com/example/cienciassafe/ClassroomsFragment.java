package com.example.cienciassafe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassroomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String[] buildings = { "Edifício C1", "Edifício C2", "Edifício C3", "Edifício C4", "Edifício C5", "Edifício C6", "Edifício C7", "Edifício C8" };
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}