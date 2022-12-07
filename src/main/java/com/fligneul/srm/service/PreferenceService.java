package com.fligneul.srm.service;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.prefs.Preferences;

/**
 * OS preference service
 */
public class PreferenceService {
    private static final String SHOOTING_RANGE_NAME_PREF = "shooting_range_name";
    private static final String MEDICAL_CERTIFICATE_VALIDITY_INFINITE = "medical_certificate_validity_infinite";
    private static final String MEDICAL_CERTIFICATE_VALIDITY_PERIOD = "medical_certificate_validity_period";
    private static final String MEDICAL_CERTIFICATE_VALIDITY_ALERT = "medical_certificate_validity_alert";
    private final Preferences prefs = Preferences.userNodeForPackage(PreferenceService.class);

    private final PublishSubject<Long> medicalCertificateValidityChangedSubject = PublishSubject.create();

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

    /**
     * Save the medical certificate validity policy
     * True is the certificate validity is infinite, false otherwise
     *
     * @param isInfinite
     *         if the medical certificate validity is infinite
     */
    public void saveMedicalCertificateValidityInfinite(final boolean isInfinite) {
        prefs.putBoolean(MEDICAL_CERTIFICATE_VALIDITY_INFINITE, isInfinite);
        medicalCertificateValidityChangedSubject.onNext(System.currentTimeMillis());
    }

    /**
     * @return the medical certificate validity policy
     */
    public boolean getMedicalCertificateValidityInfinite() {
        return prefs.getBoolean(MEDICAL_CERTIFICATE_VALIDITY_INFINITE, true);
    }

    /**
     * Save the medical certificate validity period in months
     *
     * @param period
     *         the medical certificate validity period in months
     */
    public void saveMedicalCertificateValidityPeriod(final int period) {
        prefs.putInt(MEDICAL_CERTIFICATE_VALIDITY_PERIOD, period);
        medicalCertificateValidityChangedSubject.onNext(System.currentTimeMillis());
    }

    /**
     * @return the medical certificate validity period in months
     */
    public int getMedicalCertificateValidityPeriod() {
        return prefs.getInt(MEDICAL_CERTIFICATE_VALIDITY_PERIOD, 12);
    }

    /**
     * Save the medical certificate validity alert reminder in months
     *
     * @param alertPeriod
     *         the medical certificate validity alert reminder in months
     */
    public void saveMedicalCertificateValidityAlert(final int alertPeriod) {
        prefs.putInt(MEDICAL_CERTIFICATE_VALIDITY_ALERT, alertPeriod);
        medicalCertificateValidityChangedSubject.onNext(System.currentTimeMillis());
    }

    /**
     * @return the medical certificate validity alert reminder in months
     */
    public int getMedicalCertificateValidityAlert() {
        return prefs.getInt(MEDICAL_CERTIFICATE_VALIDITY_ALERT, 11);
    }

    /**
     * @return an observable triggered when a change occur
     */
    public Observable<Long> medicalCertificateValidityChanged() {
        return medicalCertificateValidityChangedSubject.hide();
    }
}
