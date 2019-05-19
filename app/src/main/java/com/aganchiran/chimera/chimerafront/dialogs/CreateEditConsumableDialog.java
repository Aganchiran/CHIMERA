package com.aganchiran.chimera.chimerafront.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;

import java.util.Objects;

public class CreateEditConsumableDialog extends AppCompatDialogFragment {

    private CreateConsumableDialogListener listener;
    private EditText editTextName;
    private EditText editTextMaxValue;
    private EditText editTextMinValue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_edit_consumable, null);

        editTextName = view.findViewById(R.id.name_value);
        editTextMaxValue = view.findViewById(R.id.max_value);
        editTextMinValue = view.findViewById(R.id.min_value);

        final ConsumableModel consumableModel = listener.getConsumable();
        if (consumableModel != null) {
            editTextName.setText(consumableModel.getName());
            editTextMaxValue.setText(String.valueOf(consumableModel.getMaxValue()));
            editTextMinValue.setText(String.valueOf(consumableModel.getMinValue()));
            builder.setTitle("Edit consumable");
        }else {
            builder.setTitle("Create consumable");
        }

        builder.setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.accept, null);


        final AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                String maxText = editTextMaxValue.getText().toString();
                String minText = editTextMinValue.getText().toString();

                if (name.trim().isEmpty()) {
                    Toast.makeText(getContext(),
                            "Name is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (maxText.trim().isEmpty()) {
                    maxText = editTextMaxValue.getHint().toString();
                }
                if (minText.trim().isEmpty()) {
                    minText = editTextMinValue.getHint().toString();
                }

                final long max = Long.parseLong(maxText);
                final long min = Long.parseLong(minText);

                if (min >= max) {
                    Toast.makeText(getContext(),
                            "Maximum value must be greater than minimum value",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.saveConsumable(name, max, min);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void setListener(CreateConsumableDialogListener listener) {
        this.listener = listener;
    }

    public interface CreateConsumableDialogListener {
        void saveConsumable(String name, long max, long min);

        ConsumableModel getConsumable();
    }
}
