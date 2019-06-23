/*
 This file is part of CHIMERA: Companion for Humans Intending to
 Master Extreme Role Adventures ("CHIMERA").

 CHIMERA is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CHIMERA is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with CHIMERA.  If not, see <https://www.gnu.org/licenses/>.
 */

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
import com.aganchiran.chimera.chimeracore.event.EventModel;

import java.util.Objects;

public class CreateEditEventDialog extends AppCompatDialogFragment {

    private CreateEventDialogListener listener;
    private EditText editTextName;
    private EditText editTextDescription;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_edit_event, null);

        editTextName = view.findViewById(R.id.name_value);
        editTextDescription = view.findViewById(R.id.description_value);

        final EventModel eventModel = listener.getEvent();
        if (eventModel != null) {
            editTextName.setText(eventModel.getName());
            editTextDescription.setText(eventModel.getDescription());
            builder.setTitle("Edit event");
        } else {
            builder.setTitle("Create event");
        }

        builder.setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.accept, null);


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
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

                listener.saveEvent(name, description);
                dialog.dismiss();
            }
        });
    }

    public void setListener(CreateEventDialogListener listener) {
        this.listener = listener;
    }

    public interface CreateEventDialogListener {
        void saveEvent(String name, String description);

        EventModel getEvent();
    }
}
