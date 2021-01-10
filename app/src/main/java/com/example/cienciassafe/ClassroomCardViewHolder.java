package com.example.cienciassafe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClassroomCardViewHolder extends RecyclerView.ViewHolder {
    public TextView Title;
    public TextView SecondaryText;
    public Button Button;

    public ClassroomCardViewHolder(@NonNull View itemView) {
        super(itemView);
        Title = itemView.findViewById(R.id.title);
        SecondaryText = itemView.findViewById(R.id.secondary_text);
        Button = itemView.findViewById(R.id.button_report);
    }
}