package com.fligneul.srm.dao.licensee;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.LicenseeRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.exception.DataAccessException;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LicenseeDAO implements IDAO<LicenseeJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private ShootingLogbookDAO shootingLogbookDAO;

    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final ShootingLogbookDAO shootingLogbookDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.shootingLogbookDAO = shootingLogbookDAO;
    }

    @Override
    public Optional<LicenseeJfxModel> save(final LicenseeJfxModel item) {
        try {
            Optional<LicenseeJfxModel> optLicensee = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.LICENSEE)
                            .set(Tables.LICENSEE.LICENCENUMBER, item.getLicenceNumber())
                            .set(Tables.LICENSEE.FIRSTNAME, item.getFirstName())
                            .set(Tables.LICENSEE.LASTNAME, item.getLastName())
                            .set(Tables.LICENSEE.MAIDENNAME, item.getMaidenName())
                            .set(Tables.LICENSEE.DATEOFBIRTH, item.getDateOfBirth())
                            .set(Tables.LICENSEE.PLACEOFBIRTH, item.getPlaceOfBirth())
                            .set(Tables.LICENSEE.DEPARTMENTOFBIRTH, item.getDepartmentOfBirth())
                            .set(Tables.LICENSEE.COUNTRYOFBIRTH, item.getCountryOfBirth())
                            .set(Tables.LICENSEE.ADDRESS, item.getAddress())
                            .set(Tables.LICENSEE.ZIPCODE, item.getZipCode())
                            .set(Tables.LICENSEE.CITY, item.getCity())
                            .set(Tables.LICENSEE.EMAIL, item.getEmail())
                            .set(Tables.LICENSEE.PHONENUMBER, item.getPhoneNumber())
                            .set(Tables.LICENSEE.LICENCESTATE, item.getLicenceState())
                            .set(Tables.LICENSEE.MEDICALCERTIFICATEDATE, item.getMedicalCertificateDate())
                            .set(Tables.LICENSEE.IDCARDDATE, item.getIdCardDate())
                            .set(Tables.LICENSEE.IDPHOTO, item.hasIdPhoto())
                            .set(Tables.LICENSEE.FIRSTLICENCEDATE, item.getFirstLicenceDate())
                            .set(Tables.LICENSEE.SEASON, item.getSeason())
                            .set(Tables.LICENSEE.AGECATEGORY, item.getAgeCategory())
                            .set(Tables.LICENSEE.HANDISPORT, item.isHandisport())
                            .set(Tables.LICENSEE.BLACKLISTED, item.isBlacklisted())
                            .set(Tables.LICENSEE.SHOOTINGLOGBOOKID, Optional.ofNullable(item.getShootingLogbook()).map(ShootingLogbookJfxModel::getId).orElse(null))
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optLicensee.ifPresentOrElse(saved -> LOGGER.debug("Licensee {} saved with id {}", saved.getLicenceNumber(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getLicenceNumber()));
            return optLicensee;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during item save", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<LicenseeJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.LICENSEE.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee get", e);
        }
        return Optional.empty();
    }

    @Override
    public List<LicenseeJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee get", e);

        }
        return new ArrayList<>();
    }

    @Override
    public Optional<LicenseeJfxModel> update(final LicenseeJfxModel licensee) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.LICENSEE)
                    .set(Tables.LICENSEE.LICENCENUMBER, licensee.getLicenceNumber())
                    .set(Tables.LICENSEE.FIRSTNAME, licensee.getFirstName())
                    .set(Tables.LICENSEE.LASTNAME, licensee.getLastName())
                    .set(Tables.LICENSEE.MAIDENNAME, licensee.getMaidenName())
                    .set(Tables.LICENSEE.DATEOFBIRTH, licensee.getDateOfBirth())
                    .set(Tables.LICENSEE.PLACEOFBIRTH, licensee.getPlaceOfBirth())
                    .set(Tables.LICENSEE.DEPARTMENTOFBIRTH, licensee.getDepartmentOfBirth())
                    .set(Tables.LICENSEE.COUNTRYOFBIRTH, licensee.getCountryOfBirth())
                    .set(Tables.LICENSEE.ADDRESS, licensee.getAddress())
                    .set(Tables.LICENSEE.ZIPCODE, licensee.getZipCode())
                    .set(Tables.LICENSEE.CITY, licensee.getCity())
                    .set(Tables.LICENSEE.EMAIL, licensee.getEmail())
                    .set(Tables.LICENSEE.PHONENUMBER, licensee.getPhoneNumber())
                    .set(Tables.LICENSEE.LICENCESTATE, licensee.getLicenceState())
                    .set(Tables.LICENSEE.MEDICALCERTIFICATEDATE, licensee.getMedicalCertificateDate())
                    .set(Tables.LICENSEE.IDCARDDATE, licensee.getIdCardDate())
                    .set(Tables.LICENSEE.IDPHOTO, licensee.hasIdPhoto())
                    .set(Tables.LICENSEE.FIRSTLICENCEDATE, licensee.getFirstLicenceDate())
                    .set(Tables.LICENSEE.SEASON, licensee.getSeason())
                    .set(Tables.LICENSEE.AGECATEGORY, licensee.getAgeCategory())
                    .set(Tables.LICENSEE.HANDISPORT, licensee.isHandisport())
                    .set(Tables.LICENSEE.BLACKLISTED, licensee.isBlacklisted())
                    .set(Tables.LICENSEE.SHOOTINGLOGBOOKID, Optional.ofNullable(licensee.getShootingLogbook()).map(ShootingLogbookJfxModel::getId).orElse(null))
                    .where(Tables.LICENSEE.ID.eq(licensee.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(licensee.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during licensee update", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(final LicenseeJfxModel obj) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.LICENSEE)
                    .where(Tables.LICENSEE.ID.eq(obj.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during licensee deletion", e);
        }
    }

    private Stream<LicenseeJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.LICENSEE)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> LicenseeRecord.class.isAssignableFrom(record.getClass()))
                .map(LicenseeRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private LicenseeJfxModel convertToJfxModel(LicenseeRecord licenseeRecord) {
        LicenseeJfxModelBuilder builder = new LicenseeJfxModelBuilder()
                .setId(licenseeRecord.getId())
                .setFirstName(licenseeRecord.getFirstname())
                .setLastName(licenseeRecord.getLastname())
                .setDateOfBirth(licenseeRecord.getDateofbirth());

        Optional.ofNullable(licenseeRecord.getLicencenumber()).ifPresent(builder::setLicenceNumber);
        Optional.ofNullable(licenseeRecord.getMaidenname()).ifPresent(builder::setMaidenName);
        Optional.ofNullable(licenseeRecord.getSex()).ifPresent(builder::setSex);
        Optional.ofNullable(licenseeRecord.getPlaceofbirth()).ifPresent(builder::setPlaceOfBirth);
        Optional.ofNullable(licenseeRecord.getDepartmentofbirth()).ifPresent(builder::setDepartmentOfBirth);
        Optional.ofNullable(licenseeRecord.getCountryofbirth()).ifPresent(builder::setCountryOfBirth);
        Optional.ofNullable(licenseeRecord.getAddress()).ifPresent(builder::setAddress);
        Optional.ofNullable(licenseeRecord.getZipcode()).ifPresent(builder::setZipCode);
        Optional.ofNullable(licenseeRecord.getCity()).ifPresent(builder::setCity);
        Optional.ofNullable(licenseeRecord.getEmail()).ifPresent(builder::setEmail);
        Optional.ofNullable(licenseeRecord.getPhonenumber()).ifPresent(builder::setPhoneNumber);
        Optional.ofNullable(licenseeRecord.getLicencestate()).ifPresent(builder::setLicenceState);
        Optional.ofNullable(licenseeRecord.getMedicalcertificatedate()).ifPresent(builder::setMedicalCertificateDate);
        Optional.ofNullable(licenseeRecord.getIdcarddate()).ifPresent(builder::setIdCardDate);
        Optional.ofNullable(licenseeRecord.getIdphoto()).ifPresent(builder::setIdPhoto);
        Optional.ofNullable(licenseeRecord.getFirstlicencedate()).ifPresent(builder::setFirstLicenceDate);
        Optional.ofNullable(licenseeRecord.getSeason()).ifPresent(builder::setSeason);
        Optional.ofNullable(licenseeRecord.getAgecategory()).ifPresent(builder::setAgeCategory);
        Optional.ofNullable(licenseeRecord.getHandisport()).ifPresent(builder::setHandisport);
        Optional.ofNullable(licenseeRecord.getBlacklisted()).ifPresent(builder::setBlacklisted);
        Optional.ofNullable(licenseeRecord.getShootinglogbookid()).ifPresent(logbookId -> builder.setShootingLogbook(shootingLogbookDAO.getById(logbookId).orElseThrow()));

        return builder.createLicenseeJfxModel();
    }
}
