package com.example.todo_listv2.viewHolders;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.ColorUtils;
import com.example.todo_listv2.Utils.TextUtils;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.TagHeaderItem;

public class TagHeaderViewHolder extends RecyclerView.ViewHolder {
    public static final int MAX_LENGTH_TITLE = 20;
    public View tagColorView;
    public TextView tagName;
    public TextView viewMoreTask;

    public TagHeaderViewHolder(View itemView){
        super(itemView);
        this.tagColorView = itemView.findViewById(R.id.tag_color_view);
        this.tagName = itemView.findViewById(R.id.tvTagName);
        this.viewMoreTask = itemView.findViewById(R.id.view_more_task);
    }

    public void bind(TagHeaderItem item){
        Tag tag = item.getTag();

        if(tag != null){
            int color = Color.parseColor(tag.getColor());
            tagColorView.setBackgroundTintList(ColorStateList.valueOf(color));
            tagName.setText(TextUtils.ellipsize(tag.getName(), MAX_LENGTH_TITLE));
        } else {
            ColorUtils.fallbackError(tagColorView);
        }

    }
}
