package com.fligneul.srm.service;

import com.fligneul.srm.dao.attendance.AttendanceDAO;
import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import io.reactivex.rxjava3.core.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY_HYPHEN;

/**
 * {@link IExportService} implementation for XLSX export
 */
public class XLSXExportService implements IExportService {
    private static final Logger LOGGER = LogManager.getLogger(XLSXExportService.class);

    private LicenseeDAO licenseeDAO;
    private AttendanceDAO attendanceDAO;
    private List<LicenseeJfxModel> licenseeList;
    private Map<DayOfWeek, IndexedColors> daysOfWeekColorMap;
    private Map<DayOfWeek, CellStyle> daysOfWeekStyleMap;

    /**
     * Inject GUICE dependencies
     */
    @Inject
    public void injectDependencies(final LicenseeDAO licenseeDAO, final AttendanceDAO attendanceDAO) {
        this.licenseeDAO = licenseeDAO;
        this.attendanceDAO = attendanceDAO;

        daysOfWeekColorMap = new HashMap<>();
        daysOfWeekColorMap.put(DayOfWeek.MONDAY, IndexedColors.LIGHT_TURQUOISE);
        daysOfWeekColorMap.put(DayOfWeek.TUESDAY, IndexedColors.ROSE);
        daysOfWeekColorMap.put(DayOfWeek.WEDNESDAY, IndexedColors.LIGHT_CORNFLOWER_BLUE);
        daysOfWeekColorMap.put(DayOfWeek.THURSDAY, IndexedColors.LIGHT_GREEN);
        daysOfWeekColorMap.put(DayOfWeek.FRIDAY, IndexedColors.LIGHT_YELLOW);
        daysOfWeekColorMap.put(DayOfWeek.SATURDAY, IndexedColors.LIME);
        daysOfWeekColorMap.put(DayOfWeek.SUNDAY, IndexedColors.TAN);
    }

    @Override
    public Observable<Path> export(File file, List<Integer> firingPointIdList, LocalDate beginDate, LocalDate endDate) {
        return Observable.fromCallable(() -> {
            LOGGER.info("Start statistics generation from {} to {}", beginDate, endDate);
            // Get all licensee
            licenseeList = licenseeDAO.getAll(Arrays.asList(DSL.upper(Tables.LICENSEE.LASTNAME).asc(), DSL.upper(Tables.LICENSEE.FIRSTNAME).asc()));

            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(Stream.of(beginDate, endDate)
                    .map(LocalDate::getYear)
                    .map(String::valueOf)
                    .distinct()
                    .collect(Collectors.joining(EMPTY_HYPHEN)));

            createStyle(workbook);

            createHeaders(workbook, sheet);

            computePresenceForDate(workbook, sheet, firingPointIdList,
                    beginDate.datesUntil(endDate.plusDays(1))
                            .filter(date -> attendanceDAO.wasOpened(date))
                            .collect(Collectors.toList()));

            computeStats(sheet);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            writeToDisk(file, workbook);

            LOGGER.info("Generation complete");
            return file.toPath();
        });
    }

    private void createStyle(Workbook workbook) {
        daysOfWeekStyleMap = new HashMap<>();
        daysOfWeekColorMap.forEach((dayOfWeek, indexedColors) -> {
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(indexedColors.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            daysOfWeekStyleMap.put(dayOfWeek, style);
        });
    }

    private void createHeaders(XSSFWorkbook workbook, Sheet sheet) {
        Row header = sheet.createRow(2);

        CellStyle headerStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Licence");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Nom");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Prénom");
        headerCell.setCellStyle(headerStyle);

        for (int i = 0; i < licenseeList.size(); i++) {
            LicenseeJfxModel currentLicensee = licenseeList.get(i);
            Row licenseeHeader = sheet.createRow(3 + i);

            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);

            Cell licenseeHeaderCell = licenseeHeader.createCell(0);
            licenseeHeaderCell.setCellValue(currentLicensee.getLicenceNumber());
            licenseeHeaderCell.setCellStyle(style);

            licenseeHeaderCell = licenseeHeader.createCell(1);
            licenseeHeaderCell.setCellValue(currentLicensee.getLastName());
            licenseeHeaderCell.setCellStyle(style);

            licenseeHeaderCell = licenseeHeader.createCell(2);
            licenseeHeaderCell.setCellValue(currentLicensee.getFirstName());
            licenseeHeaderCell.setCellStyle(style);
        }


        sheet.createFreezePane(3, 3);
    }

    private void computePresenceForDate(XSSFWorkbook workbook, Sheet sheet, List<Integer> firingPointIdList, List<LocalDate> dates) {
        int offset = 0;

        Map<DayOfWeek, CellStyle> daysOfWeekHeaderStyleMap = new HashMap<>();
        daysOfWeekColorMap.forEach((dayOfWeek, indexedColors) -> {
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(14);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFillForegroundColor(indexedColors.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            daysOfWeekHeaderStyleMap.put(dayOfWeek, style);
        });

        Row header0 = sheet.createRow(0);
        Row header = sheet.createRow(1);
        Row header2 = sheet.getRow(2);

        for (int i = 0; i < dates.size(); i++) {

            if (i == 0 || i > 1 && dates.get(i - 1).getDayOfMonth() > dates.get(i).getDayOfMonth()) {
                if (i > 0) {
                    offset++;
                }
                CellStyle style = workbook.createCellStyle();
                XSSFFont font = workbook.createFont();
                font.setBold(true);
                font.setFontHeight(18);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.LEFT);
                Cell cell = header0.createCell(3 + i + offset);
                cell.setCellValue(dates.get(i).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
                cell.setCellStyle(style);
            }

            Cell headerCell = header.createCell(3 + i + offset);
            headerCell.setCellValue(dates.get(i).format(DateTimeFormatter.ofPattern("E")));
            headerCell.setCellStyle(daysOfWeekHeaderStyleMap.get(dates.get(i).getDayOfWeek()));

            headerCell = header2.createCell(3 + i + offset);
            headerCell.setCellValue(dates.get(i).getDayOfMonth());
            headerCell.setCellStyle(daysOfWeekHeaderStyleMap.get(dates.get(i).getDayOfWeek()));

            List<LicenseePresenceJfxModel> attendanceByDate = attendanceDAO.getByDateAndFiringPointId(dates.get(i), firingPointIdList);

            for (int j = 0; j < licenseeList.size(); j++) {
                LicenseeJfxModel currentLicensee = licenseeList.get(j);
                String value = attendanceByDate.stream()
                        .filter(pres -> pres.getLicensee().getId() == currentLicensee.getId())
                        .map(lp -> lp.getFiringPoint().getName())
                        .sorted()
                        .distinct()
                        .collect(Collectors.joining("/"));

                Row presenceHeader = sheet.getRow(3 + j);
                Cell licenseeHeaderCell = presenceHeader.createCell(3 + i + offset);
                if (!value.isEmpty()) {
                    licenseeHeaderCell.setCellValue(value);
                }
                licenseeHeaderCell.setCellStyle(daysOfWeekStyleMap.get(dates.get(i).getDayOfWeek()));
            }
        }
    }

    private void computeStats(Sheet sheet) {
        for (int i = 3; i < sheet.getLastRowNum(); i++) {
            Row currentRow = sheet.getRow(i);
            int lastCellNum = currentRow.getLastCellNum();
            Cell formulaCell = currentRow.createCell(lastCellNum + 1);
            formulaCell.setCellFormula("COUNTA(" + currentRow.getCell(3).getAddress().formatAsString() + ":" + currentRow.getCell(lastCellNum - 1).getAddress().formatAsString() + ")");
        }
    }

    private void writeToDisk(File file, XSSFWorkbook workbook) throws IOException {
        LOGGER.info("Save to disk");
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
    }

}
