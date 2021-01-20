package com.example.cienciassafe;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportCardViewHolder extends RecyclerView.ViewHolder {
    public TextView Date;
    public TextView Building;
    public TextView Room;
    public TextView Resource;

    public ReportCardViewHolder(@NonNull View itemView) {
        super(itemView);
        Date = itemView.findViewById(R.id.date);
        Building = itemView.findViewById(R.id.building);
        Room = itemView.findViewById(R.id.room);
        Resource = itemView.findViewById(R.id.resource);
    }
}