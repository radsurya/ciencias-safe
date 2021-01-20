package com.example.cienciassafe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClassroomCardViewHolder extends RecyclerView.ViewHolder {
    public TextView Room;
    public TextView MaximumCapacity;
    public TextView OccupancyText;
    public TextView LastReportedText;
    public Button Button;

    public ClassroomCardViewHolder(@NonNull View itemView) {
        super(itemView);
        Room = itemView.findViewById(R.id.room);
        MaximumCapacity= itemView.findViewById(R.id.maximum_capacity);
        OccupancyText = itemView.findViewById(R.id.occupancy_text);
        LastReportedText = itemView.findViewById(R.id.last_reported_text);
        Button = itemView.findViewById(R.id.button_report);
    }
}