package com.fligneul.srm.ui.model.licensee;

import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Licensee model for JavaFX views
 */
public class LicenseeJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty licenceNumber = new SimpleStringProperty(EMPTY);
    private final StringProperty firstName = new SimpleStringProperty(EMPTY);
    private final StringProperty lastName = new SimpleStringProperty(EMPTY);
    private final StringProperty maidenName = new SimpleStringProperty(EMPTY);
    private final StringProperty sex = new SimpleStringProperty(EMPTY);
    private final ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<>(LocalDate.EPOCH);
    private final StringProperty placeOfBirth = new SimpleStringProperty(EMPTY);
    private final StringProperty departmentOfBirth = new SimpleStringProperty(EMPTY);
    private final StringProperty countryOfBirth = new SimpleStringProperty(EMPTY);
    private final StringProperty address = new SimpleStringProperty(EMPTY);
    private final StringProperty zipCode = new SimpleStringProperty(EMPTY);
    private final StringProperty city = new SimpleStringProperty(EMPTY);
    private final StringProperty email = new SimpleStringProperty(EMPTY);
    private final StringProperty phoneNumber = new SimpleStringProperty(EMPTY);
    private final StringProperty licenceState = new SimpleStringProperty(EMPTY);
    private final ObjectProperty<LocalDate> medicalCertificateDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> idCardDate = new SimpleObjectProperty<>();
    private final BooleanProperty idPhoto = new SimpleBooleanProperty(false);
    private final ObjectProperty<LocalDate> firstLicenceDate = new SimpleObjectProperty<>();
    private final StringProperty season = new SimpleStringProperty(EMPTY);
    private final StringProperty ageCategory = new SimpleStringProperty(EMPTY);
    private final BooleanProperty handisport = new SimpleBooleanProperty(false);
    private final BooleanProperty blacklisted = new SimpleBooleanProperty(false);
    private final ObjectProperty<ShootingLogbookJfxModel> shootingLogbook = new SimpleObjectProperty<>();
    private final StringProperty photoPath = new SimpleStringProperty(EMPTY);

    protected LicenseeJfxModel(int id, @NotBlank String firstName, @NotBlank String lastName, @NotNull LocalDate dateOfBirth) {
        this.id = id;
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.dateOfBirth.set(dateOfBirth);
    }

    public int getId() {
        return id;
    }

    public String getLicenceNumber() {
        return licenceNumber.get();
    }

    public StringProperty licenceNumberProperty() {
        return licenceNumber;
    }

    public void setLicenceNumber(final String licenceNumber) {
        this.licenceNumber.set(licenceNumber);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName.set(lastName);
    }

    public String getMaidenName() {
        return maidenName.get();
    }

    public StringProperty maidenNameProperty() {
        return maidenName;
    }

    public void setMaidenName(final String maidenName) {
        this.maidenName.set(maidenName);
    }

    public String getSex() {
        return sex.get();
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getPlaceOfBirth() {
        return placeOfBirth.get();
    }

    public StringProperty placeOfBirthProperty() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(final String placeOfBirth) {
        this.placeOfBirth.set(placeOfBirth);
    }

    public String getDepartmentOfBirth() {
        return departmentOfBirth.get();
    }

    public StringProperty departmentOfBirthProperty() {
        return departmentOfBirth;
    }

    public void setDepartmentOfBirth(final String departmentOfBirth) {
        this.departmentOfBirth.set(departmentOfBirth);
    }

    public String getCountryOfBirth() {
        return countryOfBirth.get();
    }

    public StringProperty countryOfBirthProperty() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(final String countryOfBirth) {
        this.countryOfBirth.set(countryOfBirth);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(final String address) {
        this.address.set(address);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public StringProperty zipCodeProperty() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(final String city) {
        this.city.set(city);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(final String email) {
        this.email.set(email);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getLicenceState() {
        return licenceState.get();
    }

    public StringProperty licenceStateProperty() {
        return licenceState;
    }

    public void setLicenceState(final String licenceState) {
        this.licenceState.set(licenceState);
    }

    public @Null LocalDate getMedicalCertificateDate() {
        return medicalCertificateDate.get();
    }

    public ObjectProperty<LocalDate> medicalCertificateDateProperty() {
        return medicalCertificateDate;
    }

    public void setMedicalCertificateDate(final LocalDate medicalCertificateDate) {
        this.medicalCertificateDate.set(medicalCertificateDate);
    }

    public @Null LocalDate getIdCardDate() {
        return idCardDate.get();
    }

    public ObjectProperty<LocalDate> idCardDateProperty() {
        return idCardDate;
    }

    public void setIdCardDate(final LocalDate idCardDate) {
        this.idCardDate.set(idCardDate);
    }

    public boolean hasIdPhoto() {
        return idPhoto.get();
    }

    public BooleanProperty idPhotoProperty() {
        return idPhoto;
    }

    public void setIdPhoto(final boolean hasIdPhoto) {
        this.idPhoto.set(hasIdPhoto);
    }

    public @Null LocalDate getFirstLicenceDate() {
        return firstLicenceDate.get();
    }

    public ObjectProperty<LocalDate> firstLicenceDateProperty() {
        return firstLicenceDate;
    }

    public void setFirstLicenceDate(final LocalDate firstLicenceDate) {
        this.firstLicenceDate.set(firstLicenceDate);
    }

    public String getSeason() {
        return season.get();
    }

    public StringProperty seasonProperty() {
        return season;
    }

    public void setSeason(final String season) {
        this.season.set(season);
    }

    public String getAgeCategory() {
        return ageCategory.get();
    }

    public StringProperty ageCategoryProperty() {
        return ageCategory;
    }

    public void setAgeCategory(final String ageCategory) {
        this.ageCategory.set(ageCategory);
    }

    public boolean isHandisport() {
        return handisport.get();
    }

    public BooleanProperty handisportProperty() {
        return handisport;
    }

    public void setHandisport(final boolean handisport) {
        this.handisport.set(handisport);
    }

    public boolean isBlacklisted() {
        return blacklisted.get();
    }

    public BooleanProperty blacklistedProperty() {
        return blacklisted;
    }

    public void setBlacklisted(final boolean blacklisted) {
        this.blacklisted.set(blacklisted);
    }

    public ShootingLogbookJfxModel getShootingLogbook() {
        return shootingLogbook.get();
    }

    public ObjectProperty<ShootingLogbookJfxModel> shootingLogbookProperty() {
        return shootingLogbook;
    }

    public void setShootingLogbook(ShootingLogbookJfxModel shootingLogbook) {
        this.shootingLogbook.set(shootingLogbook);
    }

    public String getPhotoPath() {
        return photoPath.get();
    }

    public StringProperty photoPathProperty() {
        return photoPath;
    }

    public void setPhotoPath(final String photoPath) {
        this.photoPath.set(photoPath);
    }

    @Override
    public String toString() {
        return "LicenseeJfxModel{" +
                "id=" + id +
                ", licenceNumber=" + licenceNumber +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                '}';
    }
}
