package com.example.cienciassafe;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter used to show a simple grid of products.
 */
public class CovidReportRecyclerViewAdapter extends RecyclerView.Adapter<CovidReportCardViewHolder> {

    private List<String> reportsList;

    CovidReportRecyclerViewAdapter(List<String> reportsList) {
        this.reportsList = reportsList;
    }

    @NonNull
    @Override
    public CovidReportCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.covid_report_card, parent, false);
        return new CovidReportCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidReportCardViewHolder holder, int position) {
        if (reportsList != null && position < reportsList.size()) {
            final String report = reportsList.get(position);
            holder.Week.setText(report.split(";")[0]);
            holder.ConfirmedCases.setText(holder.itemView.getContext().getString(R.string.confirmed_cases, report.split(";")[1]));
            holder.HighRisk.setText(holder.itemView.getContext().getString(R.string.high_risk, report.split(";")[2]));
            holder.Tests.setText(holder.itemView.getContext().getString(R.string.tests, report.split(";")[3]));
        }
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}