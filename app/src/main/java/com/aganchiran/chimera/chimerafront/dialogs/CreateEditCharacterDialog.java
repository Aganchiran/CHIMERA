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
import com.aganchiran.chimera.chimeracore.character.CharacterModel;

import java.util.Objects;

public class CreateEditCharacterDialog extends AppCompatDialogFragment {

    private CreateCharacterDialogListener listener;
    private EditText editTextName;
    private EditText editTextDescription;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_edit_character, null);

        editTextName = view.findViewById(R.id.name_value);
        editTextDescription = view.findViewById(R.id.description_value);

        final CharacterModel characterModel = listener.getCharacter();
        if (characterModel != null) {
            editTextName.setText(characterModel.getName());
            editTextDescription.setText(characterModel.getDescription());
            builder.setTitle("Edit character");
        }else {
            builder.setTitle("Create character");
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
                String description = editTextDescription.getText().toString();

                if (name.trim().isEmpty()) {
                    Toast.makeText(getContext(),
                            "Name is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.saveCharacter(name,description);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void setListener(CreateCharacterDialogListener listener) {
        this.listener = listener;
    }

    public interface CreateCharacterDialogListener {
        void saveCharacter(String name, String description);

        CharacterModel getCharacter();
    }
}
