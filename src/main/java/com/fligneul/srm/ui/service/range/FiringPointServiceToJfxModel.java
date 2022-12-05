package com.fligneul.srm.ui.service.range;

import com.fligneul.srm.dao.range.CaliberDAO;
import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.dao.range.FiringPostDAO;
import com.fligneul.srm.dao.range.TargetHolderDAO;
import com.fligneul.srm.ui.model.range.CaliberJfxModel;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.range.TargetHolderJfxModel;
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
    private TargetHolderDAO targetHolderDAO;
    private CaliberDAO caliberDAO;

    @Inject
    public void injectDependencies(final FiringPostDAO firingPostDAO, final FiringPointDAO firingPointDAO, final TargetHolderDAO targetHolderDAO, final CaliberDAO caliberDAO) {
        this.firingPostDAO = firingPostDAO;
        this.firingPointDAO = firingPointDAO;
        this.targetHolderDAO = targetHolderDAO;
        this.caliberDAO = caliberDAO;

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

    public boolean saveTargetHolder(int firingPointId, TargetHolderJfxModel targetHolderJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (firingPointId != FiringPointJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(firingPointJfxModels, t -> t.getId() == firingPointId).ifPresentOrElse(
                    pointId -> {
                        if (targetHolderJfxModel.getId() != FiringPostJfxModel.DEFAULT_ID) {
                            ListUtil.getIndex(firingPointJfxModels.get(pointId).getTargetHolders(), t -> t.getId() == targetHolderJfxModel.getId())
                                    .ifPresentOrElse(
                                            postId -> {
                                                firingPointJfxModels.get(pointId).getTargetHolders().set(postId, targetHolderDAO.update(targetHolderJfxModel).orElse(targetHolderJfxModel));
                                                success.set(true);
                                            },
                                            () -> LOGGER.error("Can't update an unknown target holder")
                                    );
                        } else {
                            targetHolderDAO.save(firingPointId, targetHolderJfxModel)
                                    .ifPresentOrElse(targetHolder -> {
                                        firingPointJfxModels.get(pointId).getTargetHolders().add(targetHolder);
                                        success.set(true);
                                    }, () -> LOGGER.error("Can't create target holder"));
                        }
                    },
                    () -> LOGGER.error("Can't update an unknown target holder")
            );
        } else {
            LOGGER.error("Can't create a new target holder without a firingPointId");
        }
        return success.get();
    }

    public boolean saveCaliber(int firingPointId, CaliberJfxModel caliberJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (firingPointId != FiringPointJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(firingPointJfxModels, t -> t.getId() == firingPointId).ifPresentOrElse(
                    pointId -> {
                        if (caliberJfxModel.getId() != FiringPostJfxModel.DEFAULT_ID) {
                            ListUtil.getIndex(firingPointJfxModels.get(pointId).getCalibers(), t -> t.getId() == caliberJfxModel.getId())
                                    .ifPresentOrElse(
                                            postId -> {
                                                firingPointJfxModels.get(pointId).getCalibers().set(postId, caliberDAO.update(caliberJfxModel).orElse(caliberJfxModel));
                                                success.set(true);
                                            },
                                            () -> LOGGER.error("Can't update an unknown caliber")
                                    );
                        } else {
                            caliberDAO.save(firingPointId, caliberJfxModel)
                                    .ifPresentOrElse(caliber -> {
                                        firingPointJfxModels.get(pointId).getCalibers().add(caliber);
                                        success.set(true);
                                    }, () -> LOGGER.error("Can't create caliber"));
                        }
                    },
                    () -> LOGGER.error("Can't update an unknown caliber")
            );
        } else {
            LOGGER.error("Can't create a new caliber without a firingPointId");
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

    public void deleteTargetHolder(FiringPointJfxModel firingPointJfxModel, TargetHolderJfxModel targetHolderJfxModel) {
        if (firingPointJfxModel.getId() != -1 && targetHolderJfxModel.getId() != -1) {
            targetHolderDAO.delete(targetHolderJfxModel);
            firingPointJfxModels.stream()
                    .filter(firingPointJfxModel::equals)
                    .findFirst()
                    .ifPresent(model -> model.getTargetHolders().removeIf(targetHolderJfxModel::equals));
        } else {
            LOGGER.warn("Can't delete an unsaved target holder");
        }
    }

    public void deleteCaliber(FiringPointJfxModel firingPointJfxModel, CaliberJfxModel caliberJfxModel) {
        if (firingPointJfxModel.getId() != -1 && caliberJfxModel.getId() != -1) {
            caliberDAO.delete(caliberJfxModel);
            firingPointJfxModels.stream()
                    .filter(firingPointJfxModel::equals)
                    .findFirst()
                    .ifPresent(model -> model.getCalibers().removeIf(caliberJfxModel::equals));
        } else {
            LOGGER.warn("Can't delete an unsaved caliber");
        }
    }
}
