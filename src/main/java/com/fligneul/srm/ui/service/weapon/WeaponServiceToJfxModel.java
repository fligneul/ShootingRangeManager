package com.fligneul.srm.ui.service.weapon;

import com.fligneul.srm.dao.weapon.WeaponDAO;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.util.ListUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeaponServiceToJfxModel {
    private static final Logger LOGGER = LogManager.getLogger(WeaponServiceToJfxModel.class);

    private final ObservableList<WeaponJfxModel> weaponJfxModels = FXCollections.observableArrayList();
    private WeaponDAO weaponDAO;

    @Inject
    public void injectDependencies(final WeaponDAO weaponDAO) {
        this.weaponDAO = weaponDAO;

        weaponJfxModels.setAll(weaponDAO.getAll());
    }

    public ObservableList<WeaponJfxModel> getWeaponList() {
        return weaponJfxModels;
    }

    public boolean saveWeapon(WeaponJfxModel weaponJfxModel) {
        AtomicBoolean success = new AtomicBoolean(false);
        if (weaponJfxModel.getId() != WeaponJfxModel.DEFAULT_ID) {
            ListUtil.getIndex(weaponJfxModels, t -> t.getId() == weaponJfxModel.getId()).ifPresentOrElse(
                    id -> {
                        weaponJfxModels.set(id, weaponDAO.update(weaponJfxModel).orElse(weaponJfxModel));
                        success.set(true);
                    },
                    () -> LOGGER.error("Can't update an unknown weapon")
            );
        } else {
            weaponDAO.save(weaponJfxModel).ifPresentOrElse(weapon -> {
                weaponJfxModels.add(weapon);
                success.set(true);
            }, () -> LOGGER.error("Can't create weapon"));
        }
        return success.get();
    }

    public void deleteWeapon(WeaponJfxModel weaponJfxModel) {
        if (weaponJfxModel.getId() != -1) {
            weaponDAO.delete(weaponJfxModel);
            weaponJfxModels.removeIf(item -> item.getId() == weaponJfxModel.getId());
        } else {
            LOGGER.warn("Can't delete an unsaved weapon");
        }
    }

}
