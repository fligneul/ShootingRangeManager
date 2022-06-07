package com.fligneul.srm.ui.service.history;

import com.fligneul.srm.ui.service.attendance.AAttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import javafx.collections.FXCollections;

import javax.inject.Inject;
import java.time.LocalDate;

public class HistoryAttendanceServiceToJfxModel extends AAttendanceServiceToJfxModel {

    private AttendanceServiceToJfxModel attendanceServiceToJfxModel;

    @Inject
    public void injectDependencies(AttendanceServiceToJfxModel attendanceServiceToJfxModel) {
        this.attendanceServiceToJfxModel = attendanceServiceToJfxModel;
    }

    public void setHistoryDate(final LocalDate historyDate) {
        if (historyDate.isEqual(LocalDate.now())) {
            licenseePresenceJfxModels = attendanceServiceToJfxModel.getLicenseePresenceList();
        } else {
            licenseePresenceJfxModels = FXCollections.observableList(attendanceDAO.getByDate(historyDate));
        }

    }
}
