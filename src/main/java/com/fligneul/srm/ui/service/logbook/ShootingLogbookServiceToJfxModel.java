package com.fligneul.srm.ui.service.logbook;

import com.fligneul.srm.dao.logbook.ShootingLogbookDAO;
import com.fligneul.srm.dao.logbook.ShootingSessionDAO;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.fligneul.srm.util.ListUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShootingLogbookServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(ShootingLogbookServiceToJfxModel.class);

    private final ObservableList<ShootingLogbookJfxModel> shootingLogbookJfxModels = FXCollections.observableArrayList();
    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;
    private ShootingLogbookDAO shootingLogbookDAO;
    private ShootingSessionDAO shootingSessionDAO;

    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel, final ShootingLogbookDAO shootingLogbookDAO, final ShootingSessionDAO shootingSessionDAO) {
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
        this.shootingLogbookDAO = shootingLogbookDAO;
        this.shootingSessionDAO = shootingSessionDAO;

        shootingLogbookJfxModels.setAll(shootingLogbookDAO.getAll());
    }

    public ObservableList<ShootingLogbookJfxModel> getShootingLogbookList() {
        return shootingLogbookJfxModels;
    }

    public boolean saveShootingLogbook(ShootingLogbookJfxModel shootingLogbookJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (shootingLogbookJfxModel.getId() != ShootingLogbookJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(shootingLogbookJfxModels, t -> t.getId() == shootingLogbookJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        shootingLogbookJfxModel.getSessions().forEach(session -> saveShootingSession(shootingLogbookJfxModel.getId(), session));
                        shootingLogbookJfxModels.set(id, shootingLogbookDAO.update(shootingLogbookJfxModel).orElse(shootingLogbookJfxModel));
                        success.set(true);
                    },
                    () -> LOGGER.error("Can't update an unknown shooting logbook")
            );
        } else {
            shootingLogbookDAO.save(shootingLogbookJfxModel).ifPresentOrElse(shootingLogbook -> {
                shootingLogbookJfxModel.getSessions().forEach(session -> saveShootingSession(shootingLogbook.getId(), session));
                shootingLogbook.setSessions(FXCollections.observableList(shootingSessionDAO.getAllByLogbookId(shootingLogbook.getId())));
                shootingLogbookJfxModels.add(shootingLogbook);
                success.set(true);
            }, () -> LOGGER.error("Can't create shooting logbook"));
        }
        if (success.get()) {
            licenseeServiceToJfxModel.getLicenseeList().stream().filter(licenseeJfxModel -> licenseeJfxModel.getId() == shootingLogbookJfxModel.getLicenseeId())
                    .findFirst()
                    .ifPresentOrElse(licensee -> {
                                licensee.setShootingLogbook(shootingLogbookDAO.getByLicenseeId(licensee.getId()).orElseThrow());
                            },
                            () -> LOGGER.error("Can't update an unknown licensee"));
        }
        return success.get();
    }

    public boolean saveShootingSession(int shootingLogbookId, ShootingSessionJfxModel shootingSessionJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (shootingLogbookId != ShootingLogbookJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(shootingLogbookJfxModels, t -> t.getId() == shootingLogbookId).ifPresentOrElse(
                    pointId -> {
                        if (shootingSessionJfxModel.getId() != ShootingSessionJfxModel.DEFAULT_ID) {
                            ListUtil.getIndex(shootingLogbookJfxModels.get(pointId).getSessions(), t -> t.getId() == shootingSessionJfxModel.getId())
                                    .ifPresentOrElse(
                                            postId -> {
                                                success.set(true);
                                            },
                                            () -> LOGGER.error("Can't update an unknown shooting session")
                                    );
                        } else {
                            shootingSessionDAO.save(shootingLogbookId, shootingSessionJfxModel)
                                    .ifPresentOrElse(firingPost -> {
                                        success.set(true);
                                    }, () -> LOGGER.error("Can't create shooting session"));
                        }
                    },
                    () -> LOGGER.error("Can't update an unknown shooting session")
            );
        } else {
            LOGGER.error("Can't create a new shooting session without a shooting logbook id");
        }
        return success.get();
    }

    public void deleteShootingSession(int shootingLogbookId, ShootingSessionJfxModel shootingSessionJfxModel) {
        if (shootingLogbookId != -1 && shootingSessionJfxModel.getId() != -1) {
            shootingSessionDAO.delete(shootingSessionJfxModel);
            shootingLogbookJfxModels.stream()
                    .filter(model -> model.getId() == shootingLogbookId)
                    .findFirst()
                    .ifPresent(model -> model.getSessions().removeIf(shootingSessionJfxModel::equals));
        } else {
            LOGGER.warn("Can't delete an unsaved shooting session");
        }
    }
}
