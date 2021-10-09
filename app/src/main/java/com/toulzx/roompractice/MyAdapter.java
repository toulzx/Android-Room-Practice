package com.toulzx.roompractice;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// 此 Adapter 可理解为 RecycleView 的内容管理器

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Word> allWords = new ArrayList<>();

    private final boolean useCardView;
    private WordViewModel wordViewModel;

    public MyAdapter(boolean useCardView, WordViewModel wordViewModel) {
        this.useCardView = useCardView;
        this.wordViewModel = wordViewModel;
    }

    /* 管理 RecycleView 上的每一个条目（view） */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout constraintLayoutLeft;
        private TextView textViewNumber, textViewEnglish, textViewChinese;
        private Switch aSwitchInvisible;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // bind
            constraintLayoutLeft = itemView.findViewById(R.id.constraintLayoutLeft);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
            aSwitchInvisible = itemView.findViewById(R.id.aSwitchInvisible);
        }

    }

    /* 创建时呼叫 */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;

        if (useCardView) {
            itemView = layoutInflater.inflate(R.layout.cell_card_2, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.cell_normal_2, parent, false);
        }

        MyViewHolder holder = new MyViewHolder(itemView);

        holder.constraintLayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://dict.cn/" + holder.textViewEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.aSwitchInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 绑定在 onBindViewHolder 中保存的 `word`
                Word word = (Word) holder.itemView.getTag(R.id.word_for_view_holder);
                if (isChecked) {
                    holder.textViewChinese.setVisibility((View.GONE));
                    word.setChineseInvisible(true);
                } else {
                    holder.textViewChinese.setVisibility((View.VISIBLE));
                    word.setChineseInvisible(false);
                }
                wordViewModel.updateWords(word);
            }
        });

        return holder;

    }

    /* 绑定时呼叫 */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = allWords.get(position);
        // 创建 tag 使 `holder` 可在其它地方调用它存储的对象，这里将 word 传过去，利用唯一的 id 避免滥用 key 导致冲突
        holder.itemView.setTag(R.id.word_for_view_holder ,word);
        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getWord());
        holder.textViewChinese.setText(word.getChineseMeaning());
        if (word.isChineseInvisible()) {
            holder.textViewChinese.setVisibility((View.GONE));
            holder.aSwitchInvisible.setChecked(true);
        } else {
            holder.textViewChinese.setVisibility((View.VISIBLE));
            holder.aSwitchInvisible.setChecked(false);
        }
        // 不要在 `onBindViewHolder` 设置 listener, 因为它是频繁调用的，或不断重复新建 listener => 性能损失！
        // 将 listener 移至 onCreateViewHolder 中
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
