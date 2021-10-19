package com.fligneul.srm.ui.service.licensee;

import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.util.ListUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

public class LicenseeServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeServiceToJfxModel.class);

    private final ObservableList<LicenseeJfxModel> licenseeJfxModels = FXCollections.observableArrayList();
    private LicenseeDAO licenseeDAO;

    @Inject
    public void injectDependencies(final LicenseeDAO licenseeDAO) {
        this.licenseeDAO = licenseeDAO;

        licenseeJfxModels.setAll(licenseeDAO.getAll());
    }

    public ObservableList<LicenseeJfxModel> getLicenseeList() {
        return licenseeJfxModels;
    }

    public boolean saveLicensee(LicenseeJfxModel licenseeJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (licenseeJfxModel.getId() != -1) {
            ListUtil.getIndex(licenseeJfxModels, t -> t.getId() == licenseeJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        licenseeJfxModels.set(id, licenseeDAO.update(licenseeJfxModel).orElse(licenseeJfxModel));
                        success.set(true);
                    },
                    () -> LOGGER.error("Can't update an unknown licensee")
            );
        } else {
            licenseeDAO.save(licenseeJfxModel).ifPresentOrElse(licensee -> {
                licenseeJfxModels.add(licensee);
                success.set(true);
            }, () -> LOGGER.error("Can't create licensee"));
        }
        return success.get();
    }

    public void deleteLicensee(LicenseeJfxModel licenseeJfxModel) {
        if (licenseeJfxModel.getId() != -1) {
            licenseeDAO.delete(licenseeJfxModel);
            licenseeJfxModels.removeIf(item -> item.getId() == licenseeJfxModel.getId());
        } else {
            LOGGER.warn("Can't delete an unsaved licensee");
        }
    }
}
