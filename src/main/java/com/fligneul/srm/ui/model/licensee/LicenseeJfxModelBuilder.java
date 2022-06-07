package com.fligneul.srm.ui.model.licensee;

import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Licensee model builder for JavaFX views
 */
public class LicenseeJfxModelBuilder {
    private Integer id;
    private String licenceNumber;
    private String firstName;
    private String lastName;
    private String maidenName;
    private String sex;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String departmentOfBirth;
    private String countryOfBirth;
    private String address;
    private String zipCode;
    private String city;
    private String email;
    private String phoneNumber;
    private String licenceState;
    private LocalDate medicalCertificateDate;
    private LocalDate idCardDate;
    private Boolean idPhoto;
    private LocalDate firstLicenceDate;
    private String season;
    private String ageCategory;
    private Boolean handisport;
    private Boolean blacklisted;
    private ShootingLogbookJfxModel shootingLogbook;

    public LicenseeJfxModelBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public LicenseeJfxModelBuilder setLicenceNumber(final String licenceNumber) {
        this.licenceNumber = licenceNumber;
        return this;
    }

    public LicenseeJfxModelBuilder setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public LicenseeJfxModelBuilder setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LicenseeJfxModelBuilder setMaidenName(final String maidenName) {
        this.maidenName = maidenName;
        return this;
    }

    public LicenseeJfxModelBuilder setSex(final String sex) {
        this.sex = sex;
        return this;
    }

    public LicenseeJfxModelBuilder setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public LicenseeJfxModelBuilder setPlaceOfBirth(final String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
        return this;
    }

    public LicenseeJfxModelBuilder setDepartmentOfBirth(final String departmentOfBirth) {
        this.departmentOfBirth = departmentOfBirth;
        return this;
    }

    public LicenseeJfxModelBuilder setCountryOfBirth(final String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
        return this;
    }

    public LicenseeJfxModelBuilder setAddress(final String address) {
        this.address = address;
        return this;
    }

    public LicenseeJfxModelBuilder setZipCode(final String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public LicenseeJfxModelBuilder setCity(final String city) {
        this.city = city;
        return this;
    }

    public LicenseeJfxModelBuilder setEmail(final String email) {
        this.email = email;
        return this;
    }

    public LicenseeJfxModelBuilder setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public LicenseeJfxModelBuilder setLicenceState(final String licenceState) {
        this.licenceState = licenceState;
        return this;
    }

    public LicenseeJfxModelBuilder setMedicalCertificateDate(final LocalDate medicalCertificateDate) {
        this.medicalCertificateDate = medicalCertificateDate;
        return this;
    }

    public LicenseeJfxModelBuilder setIdCardDate(final LocalDate idCardDate) {
        this.idCardDate = idCardDate;
        return this;
    }

    public LicenseeJfxModelBuilder setIdPhoto(final Boolean idPhoto) {
        this.idPhoto = idPhoto;
        return this;
    }

    public LicenseeJfxModelBuilder setFirstLicenceDate(final LocalDate firstLicenceDate) {
        this.firstLicenceDate = firstLicenceDate;
        return this;
    }

    public LicenseeJfxModelBuilder setSeason(final String season) {
        this.season = season;
        return this;
    }

    public LicenseeJfxModelBuilder setAgeCategory(final String ageCategory) {
        this.ageCategory = ageCategory;
        return this;
    }

    public LicenseeJfxModelBuilder setHandisport(final Boolean handisport) {
        this.handisport = handisport;
        return this;
    }

    public LicenseeJfxModelBuilder setBlacklisted(final Boolean blacklisted) {
        this.blacklisted = blacklisted;
        return this;
    }

    public LicenseeJfxModelBuilder setShootingLogbook(final ShootingLogbookJfxModel shootingLogbook) {
        this.shootingLogbook = shootingLogbook;
        return this;
    }

    public LicenseeJfxModel createLicenseeJfxModel() {
        final LicenseeJfxModel licenseeJfxModel = new LicenseeJfxModel(Optional.ofNullable(id).orElse(LicenseeJfxModel.DEFAULT_ID), firstName, lastName, dateOfBirth);

        Optional.ofNullable(licenceNumber).ifPresent(licenseeJfxModel::setLicenceNumber);
        Optional.ofNullable(maidenName).ifPresent(licenseeJfxModel::setMaidenName);
        Optional.ofNullable(sex).ifPresent(licenseeJfxModel::setSex);
        Optional.ofNullable(placeOfBirth).ifPresent(licenseeJfxModel::setPlaceOfBirth);
        Optional.ofNullable(departmentOfBirth).ifPresent(licenseeJfxModel::setDepartmentOfBirth);
        Optional.ofNullable(countryOfBirth).ifPresent(licenseeJfxModel::setCountryOfBirth);
        Optional.ofNullable(address).ifPresent(licenseeJfxModel::setAddress);
        Optional.ofNullable(zipCode).ifPresent(licenseeJfxModel::setZipCode);
        Optional.ofNullable(city).ifPresent(licenseeJfxModel::setCity);
        Optional.ofNullable(email).ifPresent(licenseeJfxModel::setEmail);
        Optional.ofNullable(phoneNumber).ifPresent(licenseeJfxModel::setPhoneNumber);
        Optional.ofNullable(licenceState).ifPresent(licenseeJfxModel::setLicenceState);
        Optional.ofNullable(medicalCertificateDate).ifPresent(licenseeJfxModel::setMedicalCertificateDate);
        Optional.ofNullable(idCardDate).ifPresent(licenseeJfxModel::setIdCardDate);
        Optional.ofNullable(idPhoto).ifPresent(licenseeJfxModel::setIdPhoto);
        Optional.ofNullable(firstLicenceDate).ifPresent(licenseeJfxModel::setFirstLicenceDate);
        Optional.ofNullable(season).ifPresent(licenseeJfxModel::setSeason);
        Optional.ofNullable(ageCategory).ifPresent(licenseeJfxModel::setAgeCategory);
        Optional.ofNullable(handisport).ifPresent(licenseeJfxModel::setHandisport);
        Optional.ofNullable(blacklisted).ifPresent(licenseeJfxModel::setBlacklisted);
        Optional.ofNullable(shootingLogbook).ifPresent(licenseeJfxModel::setShootingLogbook);

        return licenseeJfxModel;
    }
}
