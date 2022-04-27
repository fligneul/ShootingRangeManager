package com.fligneul.srm.service;

import java.util.prefs.Preferences;

/**
 * OS preference service
 */
public class PreferenceService {
    private static final String SHOOTING_RANGE_NAME_PREF = "shooting_range_name";
    private final Preferences prefs = Preferences.userNodeForPackage(PreferenceService.class);

    /**
     * Save the shooting range name in OS preferences
     *
     * @param name
     *         the name of the shooting range
     */
    public void saveShootingRangeName(final String name) {
        prefs.put(SHOOTING_RANGE_NAME_PREF, name);
    }

    /**
     * @return the saved shooting range name
     */
    public String getShootingRangeName() {
        return prefs.get(SHOOTING_RANGE_NAME_PREF, "");
    }
}
