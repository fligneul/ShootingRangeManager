package com.fligneul.srm.dao.attendance;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.dao.range.CaliberDAO;
import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.dao.range.FiringPostDAO;
import com.fligneul.srm.dao.range.TargetHolderDAO;
import com.fligneul.srm.dao.status.StatusDAO;
import com.fligneul.srm.dao.weapon.WeaponDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.AttendanceRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.CaliberJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.range.TargetHolderJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.exception.DataAccessException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DAO for attendance table
 */
public class AttendanceDAO implements IDAO<LicenseePresenceJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private LicenseeDAO licenseeDAO;
    private FiringPointDAO firingPointDAO;
    private FiringPostDAO firingPostDAO;
    private TargetHolderDAO targetHolderDAO;
    private CaliberDAO caliberDAO;
    private WeaponDAO weaponDAO;
    private StatusDAO statusDAO;

    /**
     * Inject GUICE dependencies
     *
     * @param databaseConnectionService
     *         connection service to the DB
     * @param licenseeDAO
     *         DAO for licensee table
     * @param firingPointDAO
     *         DAO for firing point table
     * @param firingPostDAO
     *         DAO for firing post table
     * @param weaponDAO
     *         DAO for weapon table
     * @param statusDAO
     *         DAO for status table
     * @param targetHolderDAO
     *         DAO for target holder table
     * @param caliberDAO
     *         DAO for caliber table
     */
    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final LicenseeDAO licenseeDAO,
                                   final FiringPointDAO firingPointDAO,
                                   final FiringPostDAO firingPostDAO,
                                   final WeaponDAO weaponDAO,
                                   final StatusDAO statusDAO,
                                   final TargetHolderDAO targetHolderDAO,
                                   final CaliberDAO caliberDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.licenseeDAO = licenseeDAO;
        this.firingPointDAO = firingPointDAO;
        this.firingPostDAO = firingPostDAO;
        this.weaponDAO = weaponDAO;
        this.statusDAO = statusDAO;
        this.targetHolderDAO = targetHolderDAO;
        this.caliberDAO = caliberDAO;
    }

    /**
     * Save a licensee presence and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<LicenseePresenceJfxModel> save(@Nonnull final LicenseePresenceJfxModel item) {
        try {
            Optional<LicenseePresenceJfxModel> opt = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.ATTENDANCE)
                            .set(Tables.ATTENDANCE.LICENSEEID, item.getLicensee().getId())
                            .set(Tables.ATTENDANCE.STARTDATE, item.getStartDate())
                            .set(Tables.ATTENDANCE.STOPDATE, item.getStopDate())
                            .set(Tables.ATTENDANCE.FIRINGPOINTID, item.getFiringPoint().getId())
                            .set(Tables.ATTENDANCE.FIRINGPOSTID, Optional.ofNullable(item.getFiringPost()).map(FiringPostJfxModel::getId).orElse(null))
                            .set(Tables.ATTENDANCE.WEAPONID, Optional.ofNullable(item.getWeapon()).map(WeaponJfxModel::getId).orElse(null))
                            .set(Tables.ATTENDANCE.STATUSID, Optional.ofNullable(item.getStatus()).map(StatusJfxModel::getId).orElse(null))
                            .set(Tables.ATTENDANCE.TARGETHOLDERID, Optional.ofNullable(item.getTargetHolder()).map(TargetHolderJfxModel::getId).orElse(null))
                            .set(Tables.ATTENDANCE.CALIBERID, Optional.ofNullable(item.getCaliber()).map(CaliberJfxModel::getId).orElse(null))
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            opt.ifPresentOrElse(saved -> LOGGER.debug("Licensee presence saved with id {}", saved.getId()),
                    () -> LOGGER.error("Error during licensee presence save"));
            return opt;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during item save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a licensee presence by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link LicenseePresenceJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<LicenseePresenceJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.ATTENDANCE.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee presence get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all licensee presence for the provided date
     *
     * @param localDate
     *         the requested date
     * @return a list of all {@link LicenseePresenceJfxModel} of the provided date
     */
    public List<LicenseePresenceJfxModel> getByDate(final LocalDate localDate) {
        try {
            return internalGet(
                    Tables.ATTENDANCE.STARTDATE.greaterOrEqual(localDate.atTime(LocalTime.MIDNIGHT))
                            .and(Tables.ATTENDANCE.STARTDATE.lessThan(localDate.plusDays(1).atTime(LocalTime.MIDNIGHT))))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee presence get", e);
        }
        return new ArrayList<>();
    }

    /**
     * Return all licensee presence for the provided date and the provided firingPoints
     *
     * @param localDate
     *         the requested date
     * @param firingPointIdList
     *         the requested firingPoints list
     * @return a list of all {@link LicenseePresenceJfxModel} of the provided date and the provided firingPoints
     */
    public List<LicenseePresenceJfxModel> getByDateAndFiringPointId(final LocalDate localDate, final List<Integer> firingPointIdList) {
        try {
            return internalGet(
                    Tables.ATTENDANCE.STARTDATE.greaterOrEqual(localDate.atTime(LocalTime.MIDNIGHT))
                            .and(Tables.ATTENDANCE.STARTDATE.lessThan(localDate.plusDays(1).atTime(LocalTime.MIDNIGHT)))
                            .and(Tables.ATTENDANCE.FIRINGPOINTID.in(firingPointIdList)))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee presence get", e);
        }
        return new ArrayList<>();
    }

    /**
     * Return all licensee presence in DB
     *
     * @return a list of all {@link LicenseePresenceJfxModel}
     */
    @Override
    public List<LicenseePresenceJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee presence get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Update a licensee presence and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<LicenseePresenceJfxModel> update(@Nonnull final LicenseePresenceJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.ATTENDANCE)
                    .set(Tables.ATTENDANCE.LICENSEEID, item.getLicensee().getId())
                    .set(Tables.ATTENDANCE.STARTDATE, item.getStartDate())
                    .set(Tables.ATTENDANCE.STOPDATE, item.getStopDate())
                    .set(Tables.ATTENDANCE.FIRINGPOINTID, item.getFiringPoint().getId())
                    .set(Tables.ATTENDANCE.FIRINGPOSTID, Optional.ofNullable(item.getFiringPost()).map(FiringPostJfxModel::getId).orElse(null))
                    .set(Tables.ATTENDANCE.WEAPONID, Optional.ofNullable(item.getWeapon()).map(WeaponJfxModel::getId).orElse(null))
                    .set(Tables.ATTENDANCE.STATUSID, Optional.ofNullable(item.getStatus()).map(StatusJfxModel::getId).orElse(null))
                    .set(Tables.ATTENDANCE.TARGETHOLDERID, Optional.ofNullable(item.getTargetHolder()).map(TargetHolderJfxModel::getId).orElse(null))
                    .set(Tables.ATTENDANCE.CALIBERID, Optional.ofNullable(item.getCaliber()).map(CaliberJfxModel::getId).orElse(null))
                    .where(Tables.ATTENDANCE.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();

            return getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during item update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided licensee presence in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final LicenseePresenceJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.ATTENDANCE)
                    .where(Tables.ATTENDANCE.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during licensee presence deletion", e);
        }
    }

    /**
     * Return true if the shooting range was opened for the given date
     *
     * @param date
     *         date to check
     * @return true if the shooting range was opened
     */
    public boolean wasOpened(final LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Can't test a future date");
        }
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.ATTENDANCE)
                .where(Tables.ATTENDANCE.STARTDATE.greaterOrEqual(date.atTime(LocalTime.MIDNIGHT))
                        .and(Tables.ATTENDANCE.STARTDATE.lessThan(date.plusDays(1).atTime(LocalTime.MIDNIGHT))))
                .fetch()
                .isNotEmpty();
    }

    private Stream<LicenseePresenceJfxModel> internalGet(final Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.ATTENDANCE)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> AttendanceRecord.class.isAssignableFrom(record.getClass()))
                .map(AttendanceRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private LicenseePresenceJfxModel convertToJfxModel(final AttendanceRecord attendanceRecord) {
        LicenseePresenceJfxModelBuilder builder = new LicenseePresenceJfxModelBuilder()
                .setId(attendanceRecord.getId())
                .setLicensee(licenseeDAO.getById(attendanceRecord.getLicenseeid()).orElseThrow())
                .setStartDate(attendanceRecord.getStartdate())
                .setFiringPoint(firingPointDAO.getById(attendanceRecord.getFiringpointid()).orElseThrow());

        Optional.ofNullable(attendanceRecord.getStopdate()).ifPresent(builder::setStopDate);
        Optional.ofNullable(attendanceRecord.getFiringpostid()).ifPresent(firingPostId -> builder.setFiringPost(firingPostDAO.getById(firingPostId).orElseThrow()));
        Optional.ofNullable(attendanceRecord.getWeaponid()).ifPresent(weaponId -> builder.setWeapon(weaponDAO.getById(weaponId).orElseThrow()));
        Optional.ofNullable(attendanceRecord.getStatusid()).ifPresent(statusId -> builder.setStatus(statusDAO.getById(statusId).orElseThrow()));
        Optional.ofNullable(attendanceRecord.getTargetholderid()).ifPresent(targetSupportId -> builder.setTargetHolder(targetHolderDAO.getById(targetSupportId).orElseThrow()));
        Optional.ofNullable(attendanceRecord.getCaliberid()).ifPresent(caliberId -> builder.setCaliber(caliberDAO.getById(caliberId).orElseThrow()));

        return builder.createLicenseePresenceJfxModel();
    }

}
