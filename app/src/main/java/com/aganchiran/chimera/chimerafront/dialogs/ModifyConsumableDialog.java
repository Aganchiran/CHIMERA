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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.consumable.ConsumableModel;

import java.util.Objects;

public class ModifyConsumableDialog extends AppCompatDialogFragment {

    private static final String[] NP_VALUES = {"1", "2", "3", "4", "5", "10", "25", "50", "100",
            "500", "1K", "5K", "10K", "50K", "100K", "1M", "10M", "100M"};

    private CreateConsumableDialogListener listener;
    private ConsumableModel consumableModel;
    private TextView textValue;
    private SeekBar seekBar;
    private NumberPicker numberPicker;
    private ImageButton substractBttn;
    private ImageButton addBttn;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        final AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_modify_consumable_value, null);
        consumableModel = listener.getConsumable();
        final long minValue = consumableModel.getMinValue();
        final long maxValue = consumableModel.getMaxValue();

        textValue = view.findViewById(R.id.consumable_value);
        textValue.setText(String.valueOf(consumableModel.getCurrentValue()));

        seekBar = view.findViewById(R.id.seekBar);
        double proportionalValue = (double) (consumableModel.getCurrentValue() - minValue);
        double proportionalMax = (double) (maxValue - minValue);
        int newPercentage = (int) ((proportionalValue / proportionalMax) * 100);
        seekBar.setProgress(newPercentage);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final long value = Math.round((progress / 100.0) * (maxValue - minValue)) + minValue;
                textValue.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(NP_VALUES.length - 1);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDisplayedValues(NP_VALUES);

        substractBttn = view.findViewById(R.id.subtract_bttn);
        addBttn = view.findViewById(R.id.add_bttn);
        substractBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long newValue = Long.parseLong(textValue.getText().toString()) - getValueNP();
                if (newValue < minValue) {
                    newValue = minValue;
                }
                double proportionalValue = (double) (newValue - minValue);
                double proportionalMax = (double) (maxValue - minValue);
                int newPercentage = (int) ((proportionalValue / proportionalMax) * 100);
                seekBar.setProgress(newPercentage);
                textValue.setText(String.valueOf(newValue));
            }
        });
        addBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long newValue = Long.parseLong(textValue.getText().toString()) + getValueNP();
                if (newValue > maxValue) {
                    newValue = maxValue;
                }
                double proportionalValue = (double) (newValue - minValue);
                double proportionalMax = (double) (maxValue - minValue);
                int newPercentage = (int) ((proportionalValue / proportionalMax) * 100);
                seekBar.setProgress(newPercentage);
                textValue.setText(String.valueOf(newValue));
            }
        });


        builder.setView(view)
                .setTitle(consumableModel.getName())
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final long value = Long.parseLong(textValue.getText().toString());
                        listener.saveValue(value);
                    }
                });

        return builder.create();
    }

    private long getValueNP() {
        String npValue = NP_VALUES[numberPicker.getValue()];

        try {
            return Long.parseLong(npValue);
        } catch (NumberFormatException e) {
            int lastPos = npValue.length() - 1;
            if (npValue.charAt(lastPos) == 'K') {
                return Long.parseLong(npValue.substring(0, lastPos)) * 1000;
            } else if (npValue.charAt(lastPos) == 'M') {
                return Long.parseLong(npValue.substring(0, lastPos)) * 1000000;
            } else {
                throw new NumberFormatException(e.getMessage());
            }
        }
    }

    public void setListener(CreateConsumableDialogListener listener) {
        this.listener = listener;
    }

    public interface CreateConsumableDialogListener {
        void saveValue(long value);

        ConsumableModel getConsumable();

    }
}
