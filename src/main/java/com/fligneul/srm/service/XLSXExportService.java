package com.fligneul.srm.service;

import com.fligneul.srm.dao.attendance.AttendanceDAO;
import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY_HYPHEN;

/**
 * {@link IExportService} implementation for XLSX export
 */
public class XLSXExportService implements IExportService {
    private static final Logger LOGGER = LogManager.getLogger(XLSXExportService.class);
    private static final int HEADER_ROW_COUNT = 3;
    private static final int HEADER_COLUMN_COUNT = 3;
    private static final int LICENCE_COLUMN_ID = 0;
    private static final int LASTNAME_COLUMN_ID = 1;
    private static final int FIRSTNAME_COLUMN_ID = 2;

    private LicenseeDAO licenseeDAO;
    private AttendanceDAO attendanceDAO;
    private FiringPointDAO firingPointDAO;
    private Map<DayOfWeek, IndexedColors> daysOfWeekColorMap;

    /**
     * Inject GUICE dependencies
     */
    @Inject
    public void injectDependencies(final LicenseeDAO licenseeDAO, final AttendanceDAO attendanceDAO, final FiringPointDAO firingPointDAO) {
        this.licenseeDAO = licenseeDAO;
        this.attendanceDAO = attendanceDAO;
        this.firingPointDAO = firingPointDAO;

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
            List<LicenseeJfxModel> licenseeList = licenseeDAO.getAll(Arrays.asList(DSL.upper(Tables.LICENSEE.LASTNAME).asc(), DSL.upper(Tables.LICENSEE.FIRSTNAME).asc()));
            // Map for cell style
            Map<DayOfWeek, CellStyle> daysOfWeekStyleMap = new HashMap<>();

            // Create a XLSX workbook and sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(Stream.of(beginDate, endDate)
                    .map(LocalDate::getYear)
                    .map(String::valueOf)
                    .distinct()
                    .collect(Collectors.joining(EMPTY_HYPHEN)));

            // Create style map
            createStyle(workbook, daysOfWeekStyleMap);

            // Write selected firing points in 1st cell
            writeSettings(workbook, sheet, firingPointIdList);

            // Create table licensee headers
            createLicenseeHeaders(workbook, sheet, licenseeList);

            // Add total header on last row
            createTotalHeader(workbook, sheet, licenseeList);

            // Query the DB to retrieve presence for every registered licensee
            computePresenceForDate(workbook, sheet, firingPointIdList, licenseeList, getOpenedDateList(beginDate, endDate), daysOfWeekStyleMap);

            // Create statistics cells
            computeStats(workbook, sheet, firingPointIdList);

            // Save XLSX workbook to disk
            writeToDisk(file, workbook);

            // Close workbook
            workbook.close();

            LOGGER.info("Generation complete");
            return file.toPath();
        });
    }

    private void createStyle(Workbook workbook, Map<DayOfWeek, CellStyle> daysOfWeekStyleMap) {
        daysOfWeekStyleMap.putAll(
                daysOfWeekColorMap.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                            CellStyle style = workbook.createCellStyle();
                            style.setFillForegroundColor(e.getValue().getIndex());
                            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            addMediumBorders(style);
                            return style;
                        }))
        );
    }

    private void writeSettings(XSSFWorkbook workbook, Sheet sheet, List<Integer> firingPointIdList) {
        Row headerRow = getOrCreateRow(sheet, 0);
        Cell headerCell = headerRow.createCell(0);
        CellStyle headerStyle = workbook.createCellStyle();
        addFont(workbook, headerStyle, 14, true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        headerCell.setCellValue(firingPointIdList.stream()
                .map(id -> firingPointDAO.getById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FiringPointJfxModel::getName)
                .collect(Collectors.joining("/")));
        headerCell.setCellStyle(headerStyle);
    }

    private void createLicenseeHeaders(XSSFWorkbook workbook, Sheet sheet, List<LicenseeJfxModel> licenseeList) {
        // Create licensee header row
        Row header = getOrCreateRow(sheet, 2);

        CellStyle headerStyle = workbook.createCellStyle();
        addFont(workbook, headerStyle, 14, true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        addMediumBorders(headerStyle);

        // Create title cells
        Cell headerCell = header.createCell(LICENCE_COLUMN_ID);
        headerCell.setCellValue("Licence");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(LASTNAME_COLUMN_ID);
        headerCell.setCellValue("Nom");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(FIRSTNAME_COLUMN_ID);
        headerCell.setCellValue("Prénom");
        headerCell.setCellStyle(headerStyle);

        // Create entry for each licensee
        for (int i = 0; i < licenseeList.size(); i++) {
            LicenseeJfxModel currentLicensee = licenseeList.get(i);
            Row licenseeHeader = sheet.createRow(HEADER_ROW_COUNT + i);

            CellStyle style = workbook.createCellStyle();
            addMediumBorders(style);

            Cell licenseeHeaderCell = licenseeHeader.createCell(LICENCE_COLUMN_ID);
            licenseeHeaderCell.setCellValue(currentLicensee.getLicenceNumber());
            licenseeHeaderCell.setCellStyle(style);

            licenseeHeaderCell = licenseeHeader.createCell(LASTNAME_COLUMN_ID);
            licenseeHeaderCell.setCellValue(currentLicensee.getLastName());
            licenseeHeaderCell.setCellStyle(style);

            licenseeHeaderCell = licenseeHeader.createCell(FIRSTNAME_COLUMN_ID);
            licenseeHeaderCell.setCellValue(currentLicensee.getFirstName());
            licenseeHeaderCell.setCellStyle(style);
        }

        // Resize licensee column
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

        // Freeze table headers
        sheet.createFreezePane(HEADER_ROW_COUNT, HEADER_COLUMN_COUNT);
    }

    private void createTotalHeader(XSSFWorkbook workbook, Sheet sheet, List<LicenseeJfxModel> licenseeList) {
        Row totalHeader = sheet.createRow(HEADER_ROW_COUNT + licenseeList.size() + 1);

        CellStyle headerStyle = workbook.createCellStyle();
        addFont(workbook, headerStyle, 14, true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        addMediumBorders(headerStyle);

        Cell headerCell = totalHeader.createCell(2);
        headerCell.setCellValue("Total");
        headerCell.setCellStyle(headerStyle);
    }

    private void computePresenceForDate(XSSFWorkbook workbook, Sheet sheet, List<Integer> firingPointIdList, List<LicenseeJfxModel> licenseeList, List<LocalDate> dates, Map<DayOfWeek, CellStyle> daysOfWeekStyleMap) {
        int offset = 0;

        Map<DayOfWeek, CellStyle> daysOfWeekHeaderStyleMap = new HashMap<>();
        daysOfWeekColorMap.forEach((dayOfWeek, indexedColors) -> {
            CellStyle style = workbook.createCellStyle();
            addFont(workbook, style, 14, true);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFillForegroundColor(indexedColors.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            addMediumBorders(style);
            daysOfWeekHeaderStyleMap.put(dayOfWeek, style);
        });

        // Table header are separated in 3 row
        // 0 - Month and year
        // 1 - Day of week
        // 2 - Day of month
        Row header0 = getOrCreateRow(sheet, 0);
        Row header1 = getOrCreateRow(sheet, 1);
        Row header2 = getOrCreateRow(sheet, 2);

        for (int i = 0; i < dates.size(); i++) {

            if (i == 0 || i > 1 && dates.get(i - 1).getDayOfMonth() > dates.get(i).getDayOfMonth()) {
                // Add an empty column between each month
                if (i > 0) {
                    offset++;
                }
                CellStyle style = workbook.createCellStyle();
                addFont(workbook, style, 18, true);
                style.setAlignment(HorizontalAlignment.LEFT);
                Cell cell = header0.createCell(HEADER_COLUMN_COUNT + i + offset);
                cell.setCellValue(dates.get(i).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
                cell.setCellStyle(style);
            }

            Cell headerCell = header1.createCell(HEADER_COLUMN_COUNT + i + offset);
            headerCell.setCellValue(dates.get(i).format(DateTimeFormatter.ofPattern("E")));
            headerCell.setCellStyle(daysOfWeekHeaderStyleMap.get(dates.get(i).getDayOfWeek()));

            headerCell = header2.createCell(HEADER_COLUMN_COUNT + i + offset);
            headerCell.setCellValue(dates.get(i).getDayOfMonth());
            headerCell.setCellStyle(daysOfWeekHeaderStyleMap.get(dates.get(i).getDayOfWeek()));

            List<LicenseePresenceJfxModel> attendanceByDate = attendanceDAO.getByDateAndFiringPointId(dates.get(i), firingPointIdList);

            // Fill presence for each day
            for (int j = 0; j < licenseeList.size(); j++) {
                LicenseeJfxModel currentLicensee = licenseeList.get(j);
                String value = attendanceByDate.stream()
                        .filter(pres -> pres.getLicensee().getId() == currentLicensee.getId())
                        .map(lp -> lp.getFiringPoint().getName())
                        .sorted()
                        .distinct()
                        .collect(Collectors.joining("/"));

                Row presenceHeader = sheet.getRow(HEADER_ROW_COUNT + j);
                Cell licenseeHeaderCell = presenceHeader.createCell(HEADER_COLUMN_COUNT + i + offset);
                if (!value.isEmpty()) {
                    licenseeHeaderCell.setCellValue(value);
                }
                licenseeHeaderCell.setCellStyle(daysOfWeekStyleMap.get(dates.get(i).getDayOfWeek()));
            }

            // Add total count by day
            Row totalHeader = sheet.getRow(HEADER_ROW_COUNT + licenseeList.size() + 1);
            Cell licenseeHeaderCell = totalHeader.createCell(HEADER_COLUMN_COUNT + i + offset);
            licenseeHeaderCell.setCellFormula("COUNTA(" + sheet.getRow(HEADER_ROW_COUNT).getCell(HEADER_COLUMN_COUNT + i + offset).getAddress().formatAsString() + ":" + sheet.getRow(HEADER_ROW_COUNT + licenseeList.size() - 1).getCell(HEADER_COLUMN_COUNT + i + offset).getAddress().formatAsString() + ")");
            licenseeHeaderCell.setCellStyle(daysOfWeekStyleMap.get(dates.get(i).getDayOfWeek()));
        }
    }

    private void computeStats(XSSFWorkbook workbook, Sheet sheet, List<Integer> firingPointIdList) {

        // Add headers
        Row currentRow = getOrCreateRow(sheet, 2);
        int lastCellNum = currentRow.getLastCellNum();

        // Create style
        CellStyle style = workbook.createCellStyle();
        addFont(workbook, style, 14, true);
        addMediumBorders(style);
        style.setAlignment(HorizontalAlignment.CENTER);

        // By firingPoint
        for (int j = 0; j < firingPointIdList.size(); j++) {
            String firingPointName = firingPointDAO.getById(firingPointIdList.get(j)).orElseThrow().getName();
            Cell headerCell = currentRow.createCell(lastCellNum + j + 1);
            headerCell.setCellValue(firingPointName);
            headerCell.setCellStyle(style);
        }

        // Total
        Cell headerCell = currentRow.createCell(lastCellNum + firingPointIdList.size() + 1);
        headerCell.setCellValue("Total");
        headerCell.setCellStyle(style);

        style = workbook.createCellStyle();
        addFont(workbook, style, 12, false);
        addMediumBorders(style);
        style.setAlignment(HorizontalAlignment.CENTER);

        // Add total count by licensee
        // No total by licensee for last row (which is total by day)
        for (int i = HEADER_ROW_COUNT; i < sheet.getLastRowNum() - 1; i++) {
            currentRow = sheet.getRow(i);
            lastCellNum = currentRow.getLastCellNum();

            // Count by firingPoint
            for (int j = 0; j < firingPointIdList.size(); j++) {
                String firingPointName = firingPointDAO.getById(firingPointIdList.get(j)).orElseThrow().getName();
                Cell formulaCell = currentRow.createCell(lastCellNum + j + 1);
                formulaCell.setCellFormula("COUNTIF(" + currentRow.getCell(HEADER_COLUMN_COUNT).getAddress().formatAsString() + ":" + currentRow.getCell(lastCellNum - 1).getAddress().formatAsString() + ",\"*" + firingPointName + "*\")");
                formulaCell.setCellStyle(style);
            }

            // Total count
            Cell formulaCell = currentRow.createCell(lastCellNum + firingPointIdList.size() + 1);
            formulaCell.setCellFormula("COUNTA(" + currentRow.getCell(HEADER_COLUMN_COUNT).getAddress().formatAsString() + ":" + currentRow.getCell(lastCellNum - 1).getAddress().formatAsString() + ")");
            formulaCell.setCellStyle(style);
        }
    }

    private void writeToDisk(File file, XSSFWorkbook workbook) throws IOException {
        LOGGER.info("Save to disk");
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
    }

    private List<LocalDate> getOpenedDateList(LocalDate beginDate, LocalDate endDate) {
        return beginDate.datesUntil(endDate.plusDays(1))
                .filter(date -> attendanceDAO.wasOpened(date))
                .collect(Collectors.toList());
    }

    private void addFont(final XSSFWorkbook workbook, final CellStyle style, final int fontHeight, final boolean isBold) {
        XSSFFont font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeight(fontHeight);
        style.setFont(font);
    }

    private void addMediumBorders(final CellStyle style) {
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
    }

    private Row getOrCreateRow(Sheet sheet, int rowId) {
        return Optional.ofNullable(sheet.getRow(rowId)).orElseGet(() -> sheet.createRow(rowId));
    }

}
