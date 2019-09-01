package com.example.iustin.bluelearn.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.domain.Question;
import com.example.iustin.bluelearn.domain.QuestionType;
import com.example.iustin.bluelearn.model.IQuestion;
import com.example.iustin.bluelearn.repository.QuestionsRepository;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment implements IQuestion {

    private static final String TAG = QuestionFragment.class.getName();

    TextView txt_question_statement;
    CheckBox checkbox1;
    CheckBox checkbox2;
    CheckBox checkbox3;
    CheckBox checkbox4;
    EditText answer_text;
    FrameLayout layout_image;
    ProgressBar progressBar;
    Question question;
    int questionIndex = -1;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View intentview = inflater.inflate(R.layout.fragment_question, container, false);

        questionIndex = getArguments().getInt("index", - 1);
        question = QuestionsRepository.getQuestionByIndex(questionIndex);
        layout_image = (FrameLayout) intentview.findViewById(R.id.layout_image);
        layout_image.setVisibility(View.GONE);

        if (question != null) {
            progressBar = (ProgressBar) intentview.findViewById(R.id.progress_bar);
            txt_question_statement = (TextView) intentview.findViewById(R.id.txt_question_statement);
            txt_question_statement.setText(question.getStatement());

            answer_text = (EditText) intentview.findViewById(R.id.answer_text);
            checkbox1 = (CheckBox) intentview.findViewById(R.id.checkbox1);
            checkbox2 = (CheckBox) intentview.findViewById(R.id.checkbox2);
            checkbox3 = (CheckBox) intentview.findViewById(R.id.checkbox3);
            checkbox4 = (CheckBox) intentview.findViewById(R.id.checkbox4);

            if (QuestionType.TEXT_ANSWER.equals(question.getQuestionType())) {
                checkbox1.setVisibility(View.GONE);
                checkbox2.setVisibility(View.GONE);
                checkbox3.setVisibility(View.GONE);
                checkbox4.setVisibility(View.GONE);


                answer_text.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (Utils.selectedValues.containsKey(question.getId().toString())){
                            Utils.selectedValues.remove(question.getId().toString());
                        }
                        Utils.selectedValues.put(question.getId().toString(), answer_text.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            } else {
                answer_text.setVisibility(View.GONE);

                checkbox1.setText(question.getAnswer1());
                checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Utils.selectedValues.put(question.getId().toString(),checkbox1.getText().toString());
                        } else {
                            Utils.selectedValues.removeMapping(question.getId().toString(),checkbox1.getText().toString());
                        }
                    }
                });


                checkbox2.setText(question.getAnswer2());
                checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Utils.selectedValues.put(question.getId().toString(), checkbox2.getText().toString());
                        } else {
                            Utils.selectedValues.removeMapping(question.getId().toString(), checkbox2.getText().toString());
                        }
                    }
                });


                checkbox3.setText(question.getAnswer3());
                checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Utils.selectedValues.put(question.getId().toString(), checkbox3.getText().toString());
                        } else {
                            Utils.selectedValues.removeMapping(question.getId().toString(), checkbox3.getText().toString());
                        }
                    }
                });


                checkbox4.setText(question.getAnswer4());
                checkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Utils.selectedValues.put(question.getId().toString(),checkbox4.getText().toString());
                        } else {
                            Utils.selectedValues.removeMapping(question.getId().toString(),checkbox4.getText().toString());
                        }
                    }
                });
            }
        }


        Log.d(TAG, "oncreateview on question fragment with question: " + question.getStatement());
        return intentview;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Question getSelectedAnswer() {
        return null;
    }

    @Override
    public void showCorrectAnswer() {
        Log.d(TAG, "show correct answer on question: " + question.getStatement());

        Set<String> answers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        answers.addAll(Utils.selectedValues.get(question.getId().toString()));

        Set<String> correctAnswers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        correctAnswers.addAll(Arrays.asList(question.getRightAnswer().split(",")));

        if (question.getQuestionType() == QuestionType.TEXT_ANSWER) {
            if (answers.equals(correctAnswers)) {
                answer_text.setText(question.getRightAnswer());
                answer_text.setTextColor(Color.GREEN);
            } else {
                answer_text.setText(question.getRightAnswer());
                answer_text.setTypeface(null, Typeface.BOLD);
                answer_text.setTextColor(Color.RED);
            }
        } else {
            for (String answer : answers) {
                if (correctAnswers.contains(answer)) {
                    if(checkbox1.getText().toString().equals(answer)){
                        checkbox1.setTypeface(null, Typeface.BOLD);
                        checkbox1.setTextColor(Color.GREEN);
                    }
                    if(checkbox2.getText().toString().equals(answer)){
                        checkbox2.setTypeface(null, Typeface.BOLD);
                        checkbox2.setTextColor(Color.GREEN);
                    }
                    if(checkbox3.getText().toString().equals(answer)){
                        checkbox3.setTypeface(null, Typeface.BOLD);
                        checkbox3.setTextColor(Color.GREEN);
                    }
                    if(checkbox4.getText().toString().equals(answer)){
                        checkbox4.setTypeface(null, Typeface.BOLD);
                        checkbox4.setTextColor(Color.GREEN);
                    }
                } else {
                    if(checkbox1.getText().toString().equals(answer)){
                        checkbox1.setTypeface(null, Typeface.BOLD);
                        checkbox1.setTextColor(Color.RED);
                    }
                    if(checkbox2.getText().toString().equals(answer)){
                        checkbox2.setTypeface(null, Typeface.BOLD);
                        checkbox2.setTextColor(Color.RED);
                    }
                    if(checkbox3.getText().toString().equals(answer)){
                        checkbox3.setTypeface(null, Typeface.BOLD);
                        checkbox3.setTextColor(Color.RED);
                    }
                    if(checkbox4.getText().toString().equals(answer)){
                        checkbox4.setTypeface(null, Typeface.BOLD);
                        checkbox4.setTextColor(Color.RED);
                    }
                }
            }
            for (String correctAnswer : correctAnswers) {
                if(checkbox1.getText().toString().equals(correctAnswer)){
                    checkbox1.setTypeface(null, Typeface.BOLD);
                    checkbox1.setTextColor(Color.GREEN);
                }
                if(checkbox2.getText().toString().equals(correctAnswer)){
                    checkbox2.setTypeface(null, Typeface.BOLD);
                    checkbox2.setTextColor(Color.GREEN);
                }
                if(checkbox3.getText().toString().equals(correctAnswer)){
                    checkbox3.setTypeface(null, Typeface.BOLD);
                    checkbox3.setTextColor(Color.GREEN);
                }
                if(checkbox4.getText().toString().equals(correctAnswer)){
                    checkbox4.setTypeface(null, Typeface.BOLD);
                    checkbox4.setTextColor(Color.GREEN);
                }
            }
        }
    }

    @Override
    public void disableAnswer() {
        if (question.getQuestionType() == QuestionType.CHOICE_ANSWER) {
            checkbox1.setEnabled(false);
            checkbox2.setEnabled(false);
            checkbox3.setEnabled(false);
            checkbox4.setEnabled(false);
        } else {
            answer_text.setEnabled(false);
        }
    }

    @Override
    public void resetQuestion() {
        Log.d(TAG, "reset question on queestion : " + question.getStatement());

        if (question.getQuestionType() == QuestionType.CHOICE_ANSWER) {
            checkbox1.setEnabled(true);
            checkbox2.setEnabled(true);
            checkbox3.setEnabled(true);
            checkbox4.setEnabled(true);

            checkbox1.setChecked(false);
            checkbox2.setChecked(false);
            checkbox3.setChecked(false);
            checkbox4.setChecked(false);

            checkbox1.setTypeface(null, Typeface.NORMAL);
            checkbox2.setTypeface(null, Typeface.NORMAL);
            checkbox3.setTypeface(null, Typeface.NORMAL);
            checkbox4.setTypeface(null, Typeface.NORMAL);

            checkbox1.setTextColor(Color.BLACK);
            checkbox2.setTextColor(Color.BLACK);
            checkbox3.setTextColor(Color.BLACK);
            checkbox4.setTextColor(Color.BLACK);
        } else {
            answer_text.setEnabled(true);

            answer_text.setText("");
            answer_text.setTextColor(Color.BLACK);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

}
