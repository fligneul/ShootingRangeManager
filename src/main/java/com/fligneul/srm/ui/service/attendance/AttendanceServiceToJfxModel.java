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

public class AttendanceServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceServiceToJfxModel.class);

    private final ObservableList<LicenseePresenceJfxModel> licenseePresenceJfxModels = FXCollections.observableArrayList();
    private AttendanceDAO attendanceDAO;


    @Inject
    public void injectDependencies(final AttendanceDAO attendanceDAO) {
        this.attendanceDAO = attendanceDAO;

        licenseePresenceJfxModels.setAll(attendanceDAO.getByDate(LocalDateTime.now().toLocalDate()));
    }

    public ObservableList<LicenseePresenceJfxModel> getLicenseePresenceList() {
        return licenseePresenceJfxModels;
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

}
