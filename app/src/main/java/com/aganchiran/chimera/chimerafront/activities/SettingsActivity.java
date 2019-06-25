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

package com.aganchiran.chimera.chimerafront.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimerafront.utils.LocaleHelper;

public class SettingsActivity extends ActivityWithUpperBar {

    public static final String ENGLISH = "en";
    public static final String SPANISH = "es";

    private RadioGroup languageRadioGroup;
    private String languageCode = ENGLISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);

        languageRadioGroup = findViewById(R.id.language_radio_group);

        super.onCreate(savedInstanceState);
    }

    public void checklanguageButton(View view) {

        switch (languageRadioGroup.getCheckedRadioButtonId()) {
            case R.id.en:
                languageCode = ENGLISH;
                break;
            case R.id.es:
                languageCode = SPANISH;
                break;
            default:
                languageCode = ENGLISH;
        }

        LocaleHelper.setLocale(this, languageCode);

        recreate();
    }
}
