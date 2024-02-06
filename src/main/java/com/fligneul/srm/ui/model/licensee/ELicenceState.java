package com.fligneul.srm.ui.model.licensee;

public enum ELicenceState {
    PAID("Payé"), TO_PAY("A devoir"), NOT_RENEWED("Non renouvelé"), FORBIDDEN("Interdit"), SECOND_LICENCE("2ème licence"), OTHER("Initiation"), INTRODUCTION("Autre"), UNKNOWN("Inconnu");

    private final String text;

    ELicenceState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static ELicenceState parse(String licenceState) {
        try {
            return ELicenceState.valueOf(licenceState);
        } catch (IllegalArgumentException | NullPointerException ignore) {
            return UNKNOWN;
        }
    }
}
