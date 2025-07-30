package com.example.todo_listv2.viewHolders;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Note;

public class NoteItemViewHolder extends RecyclerView.ViewHolder {
    public TextView headerText, content;

    public NoteItemViewHolder(View itemView){
        super(itemView);
        headerText = itemView.findViewById(R.id.textTitle);
        content = itemView.findViewById(R.id.textContent);
    }

    public void bind(Note note){
        if (note.getHeader() == null || note.getHeader().isEmpty()) {
            headerText.setVisibility(View.GONE);
        } else {
            headerText.setVisibility(View.VISIBLE);
            headerText.setText(note.getHeader());
        }

        content.setText(note.getContent());

        if (itemView instanceof CardView) {
            CardView card = (CardView) itemView;

            // Hex Color
            if (note.getColor() != null && !note.getColor().isEmpty()) {
                int color = Color.parseColor(note.getColor());
                card.setCardBackgroundColor(color);
                headerText.setTextColor(Color.WHITE);
                content.setTextColor(Color.WHITE);
            }
            // background drawable ID
            else if (note.getBackgroundId() != 0) {
                Drawable bg = ContextCompat.getDrawable(itemView.getContext(), note.getBackgroundId());
                card.setBackground(bg);

                // Reset text color
                headerText.setTextColor(Color.BLACK);
                content.setTextColor(Color.BLACK);
            }
            // Default
            else {
                card.setCardBackgroundColor(Color.WHITE);
                headerText.setTextColor(Color.BLACK);
                content.setTextColor(Color.BLACK);
            }
        }
    }
}
