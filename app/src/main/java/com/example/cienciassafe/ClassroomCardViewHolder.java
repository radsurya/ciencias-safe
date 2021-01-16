package com.example.cienciassafe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClassroomCardViewHolder extends RecyclerView.ViewHolder {
    public TextView Room;
    public TextView OccupancyText;
    public Button Button;

    public ClassroomCardViewHolder(@NonNull View itemView) {
        super(itemView);
        Room = itemView.findViewById(R.id.room);
        OccupancyText = itemView.findViewById(R.id.occupancy_text);
        Button = itemView.findViewById(R.id.button_report);
    }
}