package com.fligneul.srm.dao.licensee;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.dao.logbook.ShootingLogbookDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.LicenseeRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.OrderField;
import org.jooq.exception.DataAccessException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DAO for licensee table
 */
public class LicenseeDAO implements IDAO<LicenseeJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private ShootingLogbookDAO shootingLogbookDAO;

    /**
     * Inject GUICE dependencies
     *
     * @param databaseConnectionService
     *         connection service to the DB
     */
    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final ShootingLogbookDAO shootingLogbookDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.shootingLogbookDAO = shootingLogbookDAO;
    }

    /**
     * Save a licensee and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<LicenseeJfxModel> save(@Nonnull final LicenseeJfxModel item) {
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
                            .set(Tables.LICENSEE.PHOTOPATH, item.getPhotoPath())
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

    /**
     * Return a licensee by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link LicenseeJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<LicenseeJfxModel> getById(final int id) {
        try {
            return internalGet(List.of(Tables.LICENSEE.ID.eq(id))).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all licensee in DB
     *
     * @return a list of all {@link LicenseeJfxModel}
     */
    @Override
    public List<LicenseeJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Return all licensee in DB ordered by the field in parameter
     *
     * @param orders
     *         list of OrderField for the query
     * @return an ordered list of all {@link LicenseeJfxModel}
     */
    public List<LicenseeJfxModel> getAll(List<OrderField<?>> orders) {
        try {
            return internalGet(List.of(), orders).collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Update a licensee and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<LicenseeJfxModel> update(@Nonnull final LicenseeJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.LICENSEE)
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
                    .set(Tables.LICENSEE.PHOTOPATH, item.getPhotoPath())
                    .where(Tables.LICENSEE.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during licensee update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided licensee in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final LicenseeJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.LICENSEE)
                    .set(Tables.LICENSEE.DELETED, true)
                    .where(Tables.LICENSEE.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during licensee deletion", e);
        }
    }

    private Stream<LicenseeJfxModel> internalGet() throws DataAccessException {
        return internalGet(List.of());
    }

    private Stream<LicenseeJfxModel> internalGet(Collection<Condition> conditions) throws DataAccessException {
        return internalGet(conditions, List.of());
    }

    private Stream<LicenseeJfxModel> internalGet(Collection<Condition> conditions, Collection<OrderField<?>> orders) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.LICENSEE)
                .where(Stream.concat(Stream.of(Tables.LICENSEE.DELETED.eq(false)), conditions.stream()).toArray(Condition[]::new))
                .orderBy(orders)
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
        shootingLogbookDAO.getByLicenseeId(licenseeRecord.getId()).ifPresent(builder::setShootingLogbook);
        Optional.ofNullable(licenseeRecord.getPhotopath()).ifPresent(builder::setPhotoPath);

        return builder.createLicenseeJfxModel();
    }
}
