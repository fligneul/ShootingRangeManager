package com.fligneul.srm.ui.service.attendance;

import com.fligneul.srm.dao.attendance.AttendanceDAO;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.util.ListUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AAttendanceServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(AAttendanceServiceToJfxModel.class);

    protected ObservableList<LicenseePresenceJfxModel> licenseePresenceJfxModels = FXCollections.observableArrayList();
    protected AttendanceDAO attendanceDAO;

    @Inject
    public void injectInternalDependencies(final AttendanceDAO attendanceDAO) {
        this.attendanceDAO = attendanceDAO;
    }

    public boolean saveLicenseePresence(LicenseePresenceJfxModel licenseePresenceJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (licenseePresenceJfxModel.getId() != -1) {
            ListUtil.getIndex(licenseePresenceJfxModels, t -> t.getId() == licenseePresenceJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        licenseePresenceJfxModels.set(id, attendanceDAO.update(licenseePresenceJfxModel).orElse(licenseePresenceJfxModel));
                        success.set(true);
                    },
                    () -> LOGGER.error("Can't update an unknown licensee presence")
            );
        } else {
            attendanceDAO.save(licenseePresenceJfxModel).ifPresentOrElse(licensee -> {
                licenseePresenceJfxModels.add(licensee);
                success.set(true);
            }, () -> LOGGER.error("Can't create licensee presence"));
        }
        return success.get();
    }

    public void recordLicenseeExit(LicenseePresenceJfxModel licenseePresenceJfxModel) {
        if (licenseePresenceJfxModel.getId() != -1) {
            ListUtil.getIndex(licenseePresenceJfxModels, t -> t.getId() == licenseePresenceJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        licenseePresenceJfxModel.setStopDate(LocalDateTime.now());
                        licenseePresenceJfxModels.set(id, attendanceDAO.update(licenseePresenceJfxModel).orElse(licenseePresenceJfxModel));
                    },
                    () -> LOGGER.error("Can't update exit date of unknown licensee presence")
            );
        } else {
            throw new IllegalArgumentException("Can't update stop date of and unregistered licensee");
        }
    }

    public void deleteLicenseePresence(LicenseePresenceJfxModel licenseePresenceJfxModel) {
        if (licenseePresenceJfxModel.getId() != -1) {
            attendanceDAO.delete(licenseePresenceJfxModel);
            licenseePresenceJfxModels.removeIf(item -> item.getId() == licenseePresenceJfxModel.getId());
        } else {
            LOGGER.warn("Can't delete an unsaved licensee presence");
        }
    }

    public ObservableList<LicenseePresenceJfxModel> getLicenseePresenceList() {
        return licenseePresenceJfxModels;
    }

}
