package com.example.a100daysofcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CheckAnswerDialog extends AppCompatDialogFragment {

    AnswerDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.check_answer_layout, null);

        EditText checkAnswer = view.findViewById(R.id.answer_edit_text);

        builder.setView(view)
                .setTitle("Enter your answer")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ans = checkAnswer.getText().toString();
                        listener.applyText(ans);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (AnswerDialogListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement AnswerDialogListener");
        }
    }

    public interface AnswerDialogListener {
        void applyText(String ans);
    }
}
