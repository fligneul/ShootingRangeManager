package com.fligneul.srm.ui.service.attendance;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class AttendanceServiceToJfxModel extends AAttendanceServiceToJfxModel {

    @Inject
    public void injectDependencies() {
        licenseePresenceJfxModels.setAll(attendanceDAO.getByDate(LocalDateTime.now().toLocalDate()));
    }

}
