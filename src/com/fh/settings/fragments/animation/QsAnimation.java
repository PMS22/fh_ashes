/*
 * Copyright (C) 2018 FireHound
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fh.settings.fragments.qs;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

import com.fh.settings.R;

import java.util.ArrayList;
import java.util.List;

public class QsAnimation extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PREF_TILE_ANIM_STYLE = "qs_tile_animation_style";
    private static final String PREF_TILE_ANIM_DURATION = "qs_tile_animation_duration";
    private static final String PREF_TILE_ANIM_INTERPOLATOR = "qs_tile_animation_interpolator";

    private ListPreference mTileAnimationStyle;
    private ListPreference mTileAnimationDuration;
    private ListPreference mTileAnimationInterpolator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.qs_animation);
        final ContentResolver resolver = getActivity().getContentResolver();

        mTileAnimationStyle = (ListPreference) findPreference(PREF_TILE_ANIM_STYLE);
        int tileAnimationStyle = Settings.System.getIntForUser(resolver,
                Settings.System.ANIM_TILE_STYLE, 0,
                UserHandle.USER_CURRENT);
        mTileAnimationStyle.setValue(String.valueOf(tileAnimationStyle));
        mTileAnimationStyle.setSummary(mTileAnimationStyle.getEntry());
        mTileAnimationStyle.setOnPreferenceChangeListener(this);

        mTileAnimationDuration = (ListPreference) findPreference(PREF_TILE_ANIM_DURATION);
        int tileAnimationDuration = Settings.System.getIntForUser(resolver,
                Settings.System.ANIM_TILE_DURATION, 2000,
                UserHandle.USER_CURRENT);
        mTileAnimationDuration.setValue(String.valueOf(tileAnimationDuration));
        mTileAnimationDuration.setSummary(mTileAnimationDuration.getEntry());
        mTileAnimationDuration.setEnabled(tileAnimationStyle > 0);
        mTileAnimationDuration.setOnPreferenceChangeListener(this);

        mTileAnimationInterpolator = (ListPreference) findPreference(PREF_TILE_ANIM_INTERPOLATOR);
        int tileAnimationInterpolator = Settings.System.getIntForUser(resolver,
                Settings.System.ANIM_TILE_INTERPOLATOR, 0,
                UserHandle.USER_CURRENT);
        mTileAnimationInterpolator.setValue(String.valueOf(tileAnimationInterpolator));
        mTileAnimationInterpolator.setSummary(mTileAnimationInterpolator.getEntry());
        mTileAnimationInterpolator.setEnabled(tileAnimationStyle > 0);
        mTileAnimationInterpolator.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mTileAnimationStyle) {
            int value = Integer.valueOf((String) newValue);
            int index = mTileAnimationStyle.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(resolver, Settings.System.ANIM_TILE_STYLE,
                    value, UserHandle.USER_CURRENT);
            mTileAnimationStyle.setSummary(mTileAnimationStyle.getEntries()[index]);
            mTileAnimationDuration.setEnabled(value > 0);
            mTileAnimationInterpolator.setEnabled(value > 0);
            return true;
       } else if (preference == mTileAnimationDuration) {
            int value = Integer.valueOf((String) newValue);
            int index = mTileAnimationDuration.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(resolver, Settings.System.ANIM_TILE_DURATION,
                    value, UserHandle.USER_CURRENT);
            mTileAnimationDuration.setSummary(mTileAnimationDuration.getEntries()[index]);
            return true;
       } else if (preference == mTileAnimationInterpolator) {
            int value = Integer.valueOf((String) newValue);
            int index = mTileAnimationInterpolator.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(resolver, Settings.System.ANIM_TILE_INTERPOLATOR,
                    value, UserHandle.USER_CURRENT);
            mTileAnimationInterpolator.setSummary(mTileAnimationInterpolator.getEntries()[index]);
            return true;
        }
        return false;
    }

   @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.FH_SETTINGS;
    }
}
