package com.example.iustin.bluelearn.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.domain.AnswerType;
import com.example.iustin.bluelearn.domain.Question;

import java.util.List;

public class ResultGridAdapter  extends RecyclerView.Adapter<ResultGridAdapter.MyViewHolder> {

    Context context;
    List<Question> questions;

    public ResultGridAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_result_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drawable img;
        int i = Utils.currentQuestions.indexOf(questions.get(position));
        holder.btn_question.setText(new StringBuilder("Question ").append(i + 1));
        if (Utils.getAnswerOfQuestion(position, questions) == AnswerType.CORRECT) {
            holder.btn_question.setBackgroundColor(Color.parseColor("#ff99cc00"));
            img = context.getResources().getDrawable(R.drawable.ic_check_white_24dp);
            holder.btn_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, img);
        } else if (Utils.getAnswerOfQuestion(position, questions) == AnswerType.WRONG){
            holder.btn_question.setBackgroundColor(Color.parseColor("#ffcc0000"));
            img = context.getResources().getDrawable(R.drawable.ic_clear_white_24dp);
            holder.btn_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, img);
        } else {
            holder.btn_question.setBackgroundColor(Color.parseColor("#d3d3d3"));
            img = context.getResources().getDrawable(R.drawable.ic_warning_black_24dp);
            holder.btn_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, img);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button btn_question;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_question = (Button) itemView.findViewById(R.id.btn_question);
            btn_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocalBroadcastManager.getInstance(context)
                            .sendBroadcast(new Intent(Utils.GET_BACK_FROM_RESULT).putExtra(Utils.GET_BACK_FROM_RESULT,
                                    getAdapterPosition()));
                }
            });
        }
    }
}
