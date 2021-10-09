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
        return new MyViewHolder(itemView);
    }

    /* 绑定时呼叫 */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = allWords.get(position);
        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getWord());
        holder.textViewChinese.setText(word.getChineseMeaning());
        // 如果没有以下此句，则初始化时`setChecked()`会调用我们写好的监听器，重复的操作往往会引起 bugs！
        // 比如将操作过 `switch` 的 `item` 移动到可视视图之外，再滚动回来，会发现之前的设置被还原了。
        // 原因是这些 `items` 都是可回收的，滚动过程中会调用到 setOnCheckedChangeListener
        holder.aSwitchInvisible.setOnCheckedChangeListener(null);
        if (word.isChineseInvisible()) {
            holder.textViewChinese.setVisibility((View.GONE));
            holder.aSwitchInvisible.setChecked(true);
        } else {
            holder.textViewChinese.setVisibility((View.VISIBLE));
            holder.aSwitchInvisible.setChecked(false);
        }

        // 设置监听器，使点击调用网页词典查询（在此之前 `cell.xml` 的根节点必须设定为 `clickable = true`）
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
                // 此处刷新了数据更新，会调用 MainActivity 中的 `wordViewModel.getAllWordsLive().observe()` 再次刷新，
                // 如果不做处理，切换按钮界面就会十分卡顿。
                if (isChecked) {
                    holder.textViewChinese.setVisibility((View.GONE));
                    holder.aSwitchInvisible.setChecked(true);
                } else {
                    holder.textViewChinese.setVisibility((View.VISIBLE));
                    holder.aSwitchInvisible.setChecked(false);
                }
                wordViewModel.updateWords(word);
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
