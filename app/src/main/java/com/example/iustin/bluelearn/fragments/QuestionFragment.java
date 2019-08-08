package com.example.iustin.bluelearn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.domain.Question;
import com.example.iustin.bluelearn.repository.QuestionsRepository;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    private static final String TAG = QuestionFragment.class.getName();

    TextView txt_question_statement;
    CheckBox checkbox1;
    CheckBox checkbox2;
    CheckBox checkbox3;
    CheckBox checkbox4;
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

            checkbox1 = (CheckBox) intentview.findViewById(R.id.checkbox1);
            checkbox1.setText(question.getAnswer1());
            checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Utils.selectedValues.add(checkbox1.getText().toString());
                    } else {
                        Utils.selectedValues.remove(checkbox1.getText().toString());
                    }
                }
            });

            checkbox2 = (CheckBox) intentview.findViewById(R.id.checkbox2);
            checkbox2.setText(question.getAnswer2());
            checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Utils.selectedValues.add(checkbox2.getText().toString());
                    } else {
                        Utils.selectedValues.remove(checkbox2.getText().toString());
                    }
                }
            });

            checkbox3 = (CheckBox) intentview.findViewById(R.id.checkbox3);
            checkbox3.setText(question.getAnswer3());
            checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Utils.selectedValues.add(checkbox3.getText().toString());
                    } else {
                        Utils.selectedValues.remove(checkbox3.getText().toString());
                    }
                }
            });


            checkbox4 = (CheckBox) intentview.findViewById(R.id.checkbox4);
            checkbox4.setText(question.getAnswer4());
            checkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Utils.selectedValues.add(checkbox4.getText().toString());
                    } else {
                        Utils.selectedValues.remove(checkbox4.getText().toString());
                    }
                }
            });


        }

        return intentview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
