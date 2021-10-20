package com.fligneul.srm.dao.attendance;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.dao.range.FiringPostDAO;
import com.fligneul.srm.jooq.Tables;
import com.fligneul.srm.jooq.tables.records.AttendanceRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.exception.DataAccessException;

import javax.inject.Inject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AttendanceDAO implements IDAO<LicenseePresenceJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private LicenseeDAO licenseeDAO;
    private FiringPointDAO firingPointDAO;
    private FiringPostDAO firingPostDAO;

    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final LicenseeDAO licenseeDAO,
                                   final FiringPointDAO firingPointDAO,
                                   final FiringPostDAO firingPostDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.licenseeDAO = licenseeDAO;
        this.firingPointDAO = firingPointDAO;
        this.firingPostDAO = firingPostDAO;
    }

    public Optional<LicenseePresenceJfxModel> save(final LicenseePresenceJfxModel item) {
        try {
            Optional<LicenseePresenceJfxModel> opt = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.ATTENDANCE)
                            .set(Tables.ATTENDANCE.LICENSEEID, item.getLicensee().getId())
                            .set(Tables.ATTENDANCE.STARTDATE, item.getStartDate())
                            .set(Tables.ATTENDANCE.STOPDATE, item.getStopDate())
                            .set(Tables.ATTENDANCE.FIRINGPOINTID, item.getFiringPoint().getId())
                            .set(Tables.ATTENDANCE.FIRINGPOSTID, Optional.ofNullable(item.getFiringPost()).map(FiringPostJfxModel::getId).orElse(null))
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

    public Optional<LicenseePresenceJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.ATTENDANCE.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee presence get", e);
        }
        return Optional.empty();
    }

    public List<LicenseePresenceJfxModel> getByDate(LocalDate localDate) {
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

    public List<LicenseePresenceJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during licensee presence get", e);

        }
        return new ArrayList<>();
    }

    public Optional<LicenseePresenceJfxModel> update(final LicenseePresenceJfxModel item) {
        try {
            Optional<LicenseePresenceJfxModel> opt = Optional.ofNullable(databaseConnectionService.getContext()
                            .update(Tables.ATTENDANCE)
                            .set(Tables.ATTENDANCE.LICENSEEID, item.getLicensee().getId())
                            .set(Tables.ATTENDANCE.STARTDATE, item.getStartDate())
                            .set(Tables.ATTENDANCE.STOPDATE, item.getStopDate())
                            .set(Tables.ATTENDANCE.FIRINGPOINTID, item.getFiringPoint().getId())
                            .set(Tables.ATTENDANCE.FIRINGPOSTID, Optional.ofNullable(item.getFiringPost()).map(FiringPostJfxModel::getId).orElse(null))
                            .where(Tables.ATTENDANCE.ID.eq(item.getId()))
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            opt.ifPresentOrElse(saved -> LOGGER.debug("Licensee presence updated with id {}", saved.getId()),
                    () -> LOGGER.error("Error during licensee presence update"));
            return opt;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during item update", e);
        }
        return Optional.empty();
    }

    public void delete(final LicenseePresenceJfxModel obj) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.ATTENDANCE)
                    .where(Tables.ATTENDANCE.ID.eq(obj.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during licensee presence deletion", e);
        }
    }

    private Stream<LicenseePresenceJfxModel> internalGet(Condition... conditions) throws DataAccessException {
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

    private LicenseePresenceJfxModel convertToJfxModel(AttendanceRecord attendanceRecord) {
        LicenseePresenceJfxModelBuilder builder = new LicenseePresenceJfxModelBuilder()
                .setId(attendanceRecord.getId())
                .setLicensee(licenseeDAO.getById(attendanceRecord.getLicenseeid()).orElseThrow())
                .setStartDate(attendanceRecord.getStartdate())
                .setFiringPoint(firingPointDAO.getById(attendanceRecord.getFiringpointid()).orElseThrow());

        Optional.ofNullable(attendanceRecord.getStopdate()).ifPresent(builder::setStopDate);
        Optional.ofNullable(attendanceRecord.getFiringpostid()).ifPresent(firingPostId -> builder.setFiringPost(firingPostDAO.getById(firingPostId).orElseThrow()));

        return builder.createLicenseePresenceJfxModel();
    }
}
