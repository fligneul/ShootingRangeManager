package com.fligneul.srm.ui.service.range;

import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.dao.range.FiringPostDAO;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.util.ListUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

public class FiringPointServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(FiringPointServiceToJfxModel.class);

    private final ObservableList<FiringPointJfxModel> firingPointJfxModels = FXCollections.observableArrayList();
    private FiringPointDAO firingPointDAO;
    private FiringPostDAO firingPostDAO;

    @Inject
    public void injectDependencies(final FiringPostDAO firingPostDAO, final FiringPointDAO firingPointDAO) {
        this.firingPostDAO = firingPostDAO;
        this.firingPointDAO = firingPointDAO;

        firingPointJfxModels.setAll(firingPointDAO.getAll());
    }

    public ObservableList<FiringPointJfxModel> getFiringPointList() {
        return firingPointJfxModels;
    }

    public boolean saveFiringPoint(FiringPointJfxModel firingPointJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (firingPointJfxModel.getId() != FiringPointJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(firingPointJfxModels, t -> t.getId() == firingPointJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        firingPointJfxModels.set(id, firingPointDAO.update(firingPointJfxModel).orElse(firingPointJfxModel));
                        success.set(true);
                    },
                    () -> LOGGER.error("Can't update an unknown firingPoint")
            );
        } else {
            firingPointDAO.save(firingPointJfxModel).ifPresentOrElse(licensee -> {
                firingPointJfxModels.add(licensee);
                success.set(true);
            }, () -> LOGGER.error("Can't create firingPoint"));
        }
        return success.get();
    }

    public boolean saveFiringPost(int firingPointId, FiringPostJfxModel firingPostJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (firingPointId != FiringPointJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(firingPointJfxModels, t -> t.getId() == firingPointId).ifPresentOrElse(
                    pointId -> {
                        if (firingPostJfxModel.getId() != FiringPostJfxModel.DEFAULT_ID) {
                            ListUtil.getIndex(firingPointJfxModels.get(pointId).getPosts(), t -> t.getId() == firingPostJfxModel.getId())
                                    .ifPresentOrElse(
                                            postId -> {
                                                firingPointJfxModels.get(pointId).getPosts().set(postId, firingPostDAO.update(firingPostJfxModel).orElse(firingPostJfxModel));
                                                success.set(true);
                                            },
                                            () -> LOGGER.error("Can't update an unknown firingPost")
                                    );
                        } else {
                            firingPostDAO.save(firingPointId, firingPostJfxModel)
                                    .ifPresentOrElse(firingPost -> {
                                        firingPointJfxModels.get(pointId).getPosts().add(firingPost);
                                        success.set(true);
                                    }, () -> LOGGER.error("Can't create firingPost"));
                        }
                    },
                    () -> LOGGER.error("Can't update an unknown firingPost")
            );
        } else {
            LOGGER.error("Can't create a new firingPost without a firingPointId");
        }
        return success.get();
    }

    public void deleteFiringPoint(FiringPointJfxModel firingPointJfxModel) {
        if (firingPointJfxModel.getId() != -1) {
            firingPointDAO.delete(firingPointJfxModel);
            firingPointJfxModels.removeIf(item -> item.getId() == firingPointJfxModel.getId());
        } else {
            LOGGER.warn("Can't delete an unsaved firing point");
        }
    }

    public void deleteFiringPost(FiringPointJfxModel firingPointJfxModel, FiringPostJfxModel firingPostJfxModel) {
        if (firingPointJfxModel.getId() != -1 && firingPostJfxModel.getId() != -1) {
            firingPostDAO.delete(firingPostJfxModel);
            firingPointJfxModels.stream()
                    .filter(firingPointJfxModel::equals)
                    .findFirst()
                    .ifPresent(model -> model.getPosts().removeIf(firingPostJfxModel::equals));
        } else {
            LOGGER.warn("Can't delete an unsaved firing point");
        }
    }
}
