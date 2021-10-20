package com.fligneul.srm.service;

import java.util.prefs.Preferences;

public class PreferenceService {
    private static final String SHOOTING_RANGE_NAME_PREF = "shooting_range_name";
    private final Preferences prefs = Preferences.userNodeForPackage(PreferenceService.class);

    public void saveShootingRangeName(final String name) {
        prefs.put(SHOOTING_RANGE_NAME_PREF, name);
    }

    public String getShootingRangeName() {
        return prefs.get(SHOOTING_RANGE_NAME_PREF, "");
    }
}
