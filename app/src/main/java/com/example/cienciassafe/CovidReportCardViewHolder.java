package com.example.cienciassafe;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CovidReportCardViewHolder extends RecyclerView.ViewHolder {
    public TextView Week;
    public TextView ConfirmedCases;
    public TextView HighRisk;
    public TextView Tests;

    public CovidReportCardViewHolder(@NonNull View itemView) {
        super(itemView);
        Week = itemView.findViewById(R.id.week);
        ConfirmedCases = itemView.findViewById(R.id.confirmed_cases);
        HighRisk = itemView.findViewById(R.id.high_risk);
        Tests = itemView.findViewById(R.id.tests);
    }
}