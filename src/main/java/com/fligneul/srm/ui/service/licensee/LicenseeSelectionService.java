package com.fligneul.srm.ui.service.licensee;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.annotation.Nullable;
import java.util.Optional;

public class LicenseeSelectionService {
    private final Subject<Optional<LicenseeJfxModel>> selectedLicensee = BehaviorSubject.createDefault(Optional.empty());

    public void setSelected(@Nullable final LicenseeJfxModel licenseeJfxModel) {
        selectedLicensee.onNext(Optional.ofNullable(licenseeJfxModel));
    }

    public Observable<Optional<LicenseeJfxModel>> selectedObs() {
        return selectedLicensee;
    }
}
