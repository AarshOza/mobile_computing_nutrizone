package com.example.nutrizone;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class cups_water extends AppCompatDialogFragment {

    private Button minus,plus;
    private TextView counter_text;
    int counter=0;
    private cups_water_listener listener;

    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cups_water, null);

        builder.setView(view)
                .setTitle("How many cups of Water ?")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String cups_value = counter_text.getText().toString() ;
                        listener.cups_of_water(cups_value);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

        minus = view.findViewById(R.id.minus_button);
        plus = view.findViewById(R.id.plus_button);
        counter_text = view.findViewById(R.id.counter_value);
        counter_text.setText(toString().valueOf(counter));

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter --;
                    counter_text.setText(toString().valueOf(counter));
                }
                else {
                    Toast.makeText(getActivity(), "You cannot drink less than 0 cups of water!!", Toast.LENGTH_LONG).show();
                }

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter ++;
                counter_text.setText(toString().valueOf(counter));
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (cups_water_listener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement cups_water_listener");
        }
    }

    public interface cups_water_listener {
        void cups_of_water(String cups_water);
    }
}
