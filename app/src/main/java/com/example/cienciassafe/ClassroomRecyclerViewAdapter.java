package com.example.cienciassafe;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter used to show a simple grid of products.
 */
public class ClassroomRecyclerViewAdapter extends RecyclerView.Adapter<ClassroomCardViewHolder> {

    private List<String> classroomList;

    ClassroomRecyclerViewAdapter(List<String> classroomList) {
        this.classroomList = classroomList;
    }

    @NonNull
    @Override
    public ClassroomCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.classroom_card, parent, false);
        return new ClassroomCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomCardViewHolder holder, int position) {
        if (classroomList != null && position < classroomList.size()) {
            final String classroom = classroomList.get(position);
            holder.MaximumCapacity.setText(holder.itemView.getContext().getString(R.string.maximum_capacity, classroom.split(";")[3]));
            holder.Room.setText(classroom.split(";")[0]);
            if (classroom.split(";")[1].equals("no_info")) {
                holder.OccupancyText.setText(R.string.classroom_no_info);
            } else if (classroom.split(";")[1].equals("empty")) {
                holder.OccupancyText.setText(R.string.classroom_empty);
            } else if (classroom.split(";")[1].equals("almost_empty")) {
                holder.OccupancyText.setText(R.string.classroom_almost_empty);
            } else if (classroom.split(";")[1].equals("half_full")) {
                holder.OccupancyText.setText(R.string.classroom_half_full);
            } else if (classroom.split(";")[1].equals("almost_full")) {
                holder.OccupancyText.setText(R.string.classroom_almost_full);
            } else if (classroom.split(";")[1].equals("full")) {
                holder.OccupancyText.setText(R.string.classroom_full);
            } else {
                holder.OccupancyText.setText(R.string.classroom_error);
            }
            if (classroom.split(";")[2].equals("no_info")) {
                holder.LastReportedText.setText(R.string.last_reported_no_info);
            } else {
                holder.LastReportedText.setText(holder.itemView.getContext().getString(R.string.last_reported, classroom.split(";")[2]));
            }
            holder.Button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String[] singleItems = {v.getResources().getString(R.string.empty), v.getResources().getString(R.string.almost_empty), v.getResources().getString(R.string.half_full), v.getResources().getString(R.string.almost_full), v.getResources().getString(R.string.full)};
                    System.out.println(singleItems);
                    int checkedItem;
                    if (classroom.split(";")[1].equals("empty")) {
                        checkedItem = 0;
                    } else if (classroom.split(";")[1].equals("almost_empty")) {
                        checkedItem = 1;
                    } else if (classroom.split(";")[1].equals("half_full")) {
                        checkedItem = 2;
                    } else if (classroom.split(";")[1].equals("almost_full")) {
                        checkedItem = 3;
                    } else if (classroom.split(";")[1].equals("full")) {
                        checkedItem = 4;
                    } else {
                         checkedItem = 0;
                    }
                    AlertDialog dialogBuilder = new MaterialAlertDialogBuilder(v.getContext())
                            .setTitle(classroom.split(";")[0] + " " + v.getResources().getString(R.string.occupation))
                            .setSingleChoiceItems(singleItems, checkedItem, null)
                            .setPositiveButton(v.getResources().getString(R.string.report), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    // Do something useful withe the position of the selected radio button
                                    DatabaseReference reff;
                                    reff = FirebaseDatabase.getInstance().getReference();
                                    String room = classroom.split(";")[0].split(" ")[1].replace(".", "-");
                                    String building = room.split("-")[0];
                                    Map<String, Object> map = new HashMap<>();
                                    if (selectedPosition == 0) {
                                        map.put("occupation", "empty");
                                    } else if (selectedPosition == 1) {
                                        map.put("occupation", "almost_empty");
                                    } else if (selectedPosition == 2) {
                                        map.put("occupation", "half_full");
                                    } else if (selectedPosition == 3) {
                                        map.put("occupation", "almost_full");
                                    } else if (selectedPosition == 4) {
                                        map.put("occupation", "full");
                                    }

                                    String currentDateandTime = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
                                    map.put("time_report", currentDateandTime);
                                    reff.child("rooms").child("c" + building).child(room).updateChildren(map);
                                }
                            })
                            .setNegativeButton(v.getResources().getString(R.string.cancel), null)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }
}