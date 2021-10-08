package com.toulzx.roompractice;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// 此 Adapter 可理解为 RecycleView 的内容管理器

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Word> allWords = new ArrayList<>();

    private final boolean useCardView;

    public MyAdapter(boolean useCardView) {
        this.useCardView = useCardView;
    }

    /* 管理 RecycleView 上的每一个条目（view） */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNumber, textViewEnglish, textViewChinese;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // bind
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);

        }

    }

    /* 创建时呼叫 */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (useCardView) {
            itemView = layoutInflater.inflate(R.layout.cell_card, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.cell_normal, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    /* 绑定时呼叫 */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = allWords.get(position);
        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getWord());
        holder.textViewChinese.setText(word.getChineseMeaning());

        // 设置监听器，使点击调用网页词典查询（在此之前 `cell.xml` 的根节点必须设定为 `clickable = true`）
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://dict.cn/" + holder.textViewEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    /* 返回列表总的数据个数 */
    @Override
    public int getItemCount() {
        return allWords.size();
    }



    /* setter */
    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

}
