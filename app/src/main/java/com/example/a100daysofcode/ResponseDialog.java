package com.example.a100daysofcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ResponseDialog extends AppCompatDialogFragment {

    boolean correct;

    public ResponseDialog(boolean correct) {
        this.correct = correct;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.correct_answer_dialog, null);

        ImageView correctImg = view.findViewById(R.id.right_answer_img);
        ImageView wrongImg = view.findViewById(R.id.wrong_answer_img);
        if (correct) {
            correctImg.setVisibility(View.VISIBLE);
            wrongImg.setVisibility(View.GONE);
        } else {
            correctImg.setVisibility(View.GONE);
            wrongImg.setVisibility(View.VISIBLE);
        }
        builder.setView(view);

        return builder.create();
    }
}
