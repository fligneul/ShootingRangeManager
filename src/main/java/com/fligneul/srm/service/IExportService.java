package com.fligneul.srm.service;

import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for data export
 */
public interface IExportService {
    /**
     * Export presence data in a file for the given date and firing points
     *
     * @param file
     *         export file
     * @param firingPointIdList
     *         list of points to export
     * @param beginDate
     *         begin date of export
     * @param endDate
     *         end date of export
     * @return an {@link Observable} of the export file path
     */
    Observable<Path> export(File file, List<Integer> firingPointIdList, LocalDate beginDate, LocalDate endDate);
}
