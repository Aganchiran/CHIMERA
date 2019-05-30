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
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;

import java.util.Objects;

public class CreateEditCampaignDialog extends AppCompatDialogFragment {

    private CreateCampaignDialogListener listener;
    private EditText editTextName;
    private EditText editTextDescription;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_edit_campaign, null);

        editTextName = view.findViewById(R.id.name_value);
        editTextDescription = view.findViewById(R.id.description_value);

        final CampaignModel campaignModel = listener.getCampaign();
        if (campaignModel != null) {
            editTextName.setText(campaignModel.getName());
            editTextDescription.setText(campaignModel.getDescription());
            builder.setTitle("Edit campaign");
        }else {
            builder.setTitle("Create campaign");
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
                final String description = editTextDescription.getText().toString();


                if (name.trim().isEmpty()) {
                    Toast.makeText(getContext(),
                            "Name is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.saveCampaign(name, description);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void setListener(CreateCampaignDialogListener listener) {
        this.listener = listener;
    }

    public interface CreateCampaignDialogListener {
        void saveCampaign(String name, String description);

        CampaignModel getCampaign();
    }
}
