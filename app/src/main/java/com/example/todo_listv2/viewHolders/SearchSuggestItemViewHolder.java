package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;

public class SearchSuggestItemViewHolder extends RecyclerView.ViewHolder {
    public TextView searchSuggest;

    public SearchSuggestItemViewHolder(View itemView){
        super(itemView);
        searchSuggest = itemView.findViewById(R.id.search_suggest_item);
    }
}
