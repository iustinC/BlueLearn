package com.example.iustin.bluelearn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.activities.QuizActivity;
import com.example.iustin.bluelearn.domain.DifficultyType;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DifficultyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DifficultyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DifficultyFragment extends Fragment {

    RadioGroup difficultyGroup;
    RadioButton selectedDifficulty;
    Button btnStart;

    private OnFragmentInteractionListener mListener;

    public DifficultyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DifficultyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DifficultyFragment newInstance(String param1, String param2) {
        DifficultyFragment fragment = new DifficultyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_difficulty, container, false);

        difficultyGroup = view.findViewById(R.id.btnDifficultyGroup);
        difficultyGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            selectedDifficulty = view.findViewById(i);
        });

        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener((click) -> {
            if (selectedDifficulty != null) {
                Utils.selectedDifficulty = DifficultyType.valueOf(selectedDifficulty.getText().toString());
                getFragmentManager().beginTransaction()
                        .remove(DifficultyFragment.this).commit();
                ((QuizActivity) getActivity()).startQuiz();
            } else {
                Toast.makeText(getActivity(), "You need to select a difficulty.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

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
