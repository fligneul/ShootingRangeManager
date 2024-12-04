package com.fligneul.srm.service;

import com.fligneul.srm.dao.attendance.AttendanceDAO;
import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class XLSXExportServiceTest {
    private XLSXExportService xlsxExportService;

    private final LicenseeDAO licenseeDAOMock = Mockito.mock(LicenseeDAO.class);
    private final AttendanceDAO attendanceDAOMock = Mockito.mock(AttendanceDAO.class);
    private final FiringPointDAO firingPointDAOMock = Mockito.mock(FiringPointDAO.class);

    private final Path tmpDirectory = Path.of(System.getenv("TMP"), "ShootingRangeManager_" + RandomStringUtils.randomAlphanumeric(16).toUpperCase());

    @BeforeEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setUp() {
        tmpDirectory.toFile().mkdirs();

        xlsxExportService = new XLSXExportService();
        xlsxExportService.injectDependencies(licenseeDAOMock, attendanceDAOMock, firingPointDAOMock);
    }

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void cleanUp() {
        for (File file : Objects.requireNonNull(tmpDirectory.toFile().listFiles())) {
            file.delete();
        }
        tmpDirectory.toFile().delete();
    }

    @Test
    void exportTest() throws IOException {
        List<LicenseeJfxModel> licenseeJfxModelList = List.of(
                (new LicenseeJfxModelBuilder()).setId(1).setLicenceNumber("123").setFirstName("John").setLastName("Doe").createLicenseeJfxModel(),
                (new LicenseeJfxModelBuilder()).setId(2).setLicenceNumber("456").setFirstName("James").setLastName("Bond").createLicenseeJfxModel(),
                (new LicenseeJfxModelBuilder()).setId(3).setLicenceNumber("789").setFirstName("Richard ").setLastName("Roe").createLicenseeJfxModel()
        );
        Mockito.when(licenseeDAOMock.getAll(ArgumentMatchers.anyList())).thenReturn(licenseeJfxModelList);

        List<FiringPointJfxModel> firingPointJfxModelList = List.of(
                new FiringPointJfxModel(1, "POINT_A"),
                new FiringPointJfxModel(2, "POINT_B"),
                new FiringPointJfxModel(3, "POINT_C")
        );
        Mockito.when(firingPointDAOMock.getById(1)).thenReturn(Optional.of(firingPointJfxModelList.get(0)));
        Mockito.when(firingPointDAOMock.getById(2)).thenReturn(Optional.of(firingPointJfxModelList.get(1)));
        Mockito.when(firingPointDAOMock.getById(3)).thenReturn(Optional.of(firingPointJfxModelList.get(2)));

        List<LicenseePresenceJfxModel> attendanceFirstDay = List.of(
                (new LicenseePresenceJfxModelBuilder()).setId(1).setLicensee(licenseeJfxModelList.get(0)).setStartDate(LocalDateTime.of(2024, 7, 14, 10, 0)).setStopDate(LocalDateTime.of(2024, 7, 14, 12, 0)).setFiringPoint(firingPointJfxModelList.get(0)).createLicenseePresenceJfxModel(),
                (new LicenseePresenceJfxModelBuilder()).setId(2).setLicensee(licenseeJfxModelList.get(1)).setStartDate(LocalDateTime.of(2024, 7, 14, 15, 0)).setStopDate(LocalDateTime.of(2024, 7, 14, 17, 0)).setFiringPoint(firingPointJfxModelList.get(2)).createLicenseePresenceJfxModel()
        );
        List<LicenseePresenceJfxModel> attendanceSecondDay = List.of(
                (new LicenseePresenceJfxModelBuilder()).setId(3).setLicensee(licenseeJfxModelList.get(0)).setStartDate(LocalDateTime.of(2024, 7, 15, 8, 0)).setStopDate(LocalDateTime.of(2024, 7, 15, 9, 30)).setFiringPoint(firingPointJfxModelList.get(1)).createLicenseePresenceJfxModel(),
                (new LicenseePresenceJfxModelBuilder()).setId(4).setLicensee(licenseeJfxModelList.get(2)).setStartDate(LocalDateTime.of(2024, 7, 15, 16, 0)).setStopDate(LocalDateTime.of(2024, 7, 15, 17, 0)).setFiringPoint(firingPointJfxModelList.get(0)).createLicenseePresenceJfxModel()
        );
        Mockito.when(attendanceDAOMock.wasOpened(LocalDate.of(2024, 7, 14))).thenReturn(true);
        Mockito.when(attendanceDAOMock.wasOpened(LocalDate.of(2024, 7, 15))).thenReturn(true);
        Mockito.when(attendanceDAOMock.wasOpened(LocalDate.of(2024, 7, 16))).thenReturn(false);
        Mockito.when(attendanceDAOMock.getByDateAndFiringPointId(LocalDate.of(2024, 7, 14), List.of(1, 3))).thenReturn(attendanceFirstDay);
        Mockito.when(attendanceDAOMock.getByDateAndFiringPointId(LocalDate.of(2024, 7, 15), List.of(1, 3))).thenReturn(attendanceSecondDay);

        File outputFile = tmpDirectory.resolve("exportFile.xlsx").toFile();
        Assertions.assertFalse(outputFile.exists());

        Path outputFilePath = xlsxExportService.export(outputFile, List.of(1, 3), LocalDate.of(2024, 7, 14), LocalDate.of(2024, 7, 16));
        Assertions.assertTrue(outputFile.exists());
        Assertions.assertEquals(outputFile.toPath(), outputFilePath);
        Assertions.assertTrue(Files.size(outputFilePath) > 4239);
    }

}