package com.fligneul.srm.ui.service.status;

import com.fligneul.srm.dao.status.StatusDAO;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.util.ListUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

public class StatusServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(StatusServiceToJfxModel.class);

    private final ObservableList<StatusJfxModel> statusJfxModels = FXCollections.observableArrayList();
    private StatusDAO statusDAO;

    @Inject
    public void injectDependencies(final StatusDAO statusDAO) {
        this.statusDAO = statusDAO;

        statusJfxModels.setAll(statusDAO.getAll());
    }

    public ObservableList<StatusJfxModel> getStatusList() {
        return statusJfxModels;
    }

    public boolean saveStatus(StatusJfxModel statusJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (statusJfxModel.getId() != StatusJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(statusJfxModels, t -> t.getId() == statusJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        statusJfxModels.set(id, statusDAO.update(statusJfxModel).orElse(statusJfxModel));
                        success.set(true);
                    },
                    () -> LOGGER.error("Can't update an unknown status")
            );
        } else {
            statusDAO.save(statusJfxModel).ifPresentOrElse(status -> {
                statusJfxModels.add(status);
                success.set(true);
            }, () -> LOGGER.error("Can't create status"));
        }
        return success.get();
    }

    public void deleteStatus(StatusJfxModel statusJfxModel) {
        if (statusJfxModel.getId() != -1) {
            statusDAO.delete(statusJfxModel);
            statusJfxModels.removeIf(item -> item.getId() == statusJfxModel.getId());
        } else {
            LOGGER.warn("Can't delete an unsaved status");
        }
    }

}
