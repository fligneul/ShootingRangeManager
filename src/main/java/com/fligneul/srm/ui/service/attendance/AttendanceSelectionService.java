package com.fligneul.srm.ui.service.attendance;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class AttendanceSelectionService {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceSelectionService.class);

    private final BehaviorSubject<Optional<LicenseeJfxModel>> selectedLicensee = BehaviorSubject.createDefault(Optional.empty());
    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;

    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel) {
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
    }

    public boolean searchAndSelect(final String licenceNumber) {
        LOGGER.info("Search licence {}", licenceNumber);
        Optional<LicenseeJfxModel> licensee = licenseeServiceToJfxModel.getLicenseeList().stream().filter(l -> l.getLicenceNumber().equals(licenceNumber)).findFirst();
        if (licensee.isPresent()) {
            selectedLicensee.onNext(licensee);
            return true;
        } else {
            LOGGER.warn("No licensee registered with the licence {}", licenceNumber);
            return false;
        }
    }

    public void clearSelected() {
        LOGGER.info("Clear selected licensee");
        selectedLicensee.onNext(Optional.empty());
    }

    public Observable<Optional<LicenseeJfxModel>> selectedObs() {
        return selectedLicensee;
    }

}
