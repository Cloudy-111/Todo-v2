package com.example.todo_listv2.viewHolders;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteItemViewHolder extends RecyclerView.ViewHolder {
    public TextView headerText, content;

    public NoteItemViewHolder(View itemView){
        super(itemView);
        headerText = itemView.findViewById(R.id.textTitle);
        content = itemView.findViewById(R.id.textContent);
    }

    public void bind(Note note, List<Integer> backgrounds){
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
            else if (note.getBackgroundId() != 0 && note.getColor().isEmpty()) {
                ImageView imageBackground = itemView.findViewById(R.id.imageBackground);
                Drawable bg = ContextCompat.getDrawable(itemView.getContext(), backgrounds.get(note.getBackgroundId()));

                imageBackground.setImageDrawable(bg);
                imageBackground.setVisibility(View.VISIBLE);

                // Reset text color
                int index = note.getBackgroundId();
                if(index == 4 || index == 6) {
                    setColorTint(Color.BLACK);
                } else {
                    setColorTint(Color.WHITE);
                }
            }
            // Default
            else {
                card.setCardBackgroundColor(Color.WHITE);
                headerText.setTextColor(Color.BLACK);
                content.setTextColor(Color.BLACK);
            }
        }
    }

    private void setColorTint(int color){
        headerText.setTextColor(ColorStateList.valueOf(color));
        content.setTextColor(ColorStateList.valueOf(color));
    }
}
