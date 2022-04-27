package com.fligneul.srm.ui.model.presence;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

/**
 * Licensee presence model for JavaFX views
 */
public class LicenseePresenceJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final LicenseeJfxModel licensee;
    private final ObjectProperty<LocalDateTime> startDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> stopDate = new SimpleObjectProperty<>();
    private final ObjectProperty<FiringPointJfxModel> firingPoint = new SimpleObjectProperty<>();
    private final ObjectProperty<FiringPostJfxModel> firingPost = new SimpleObjectProperty<>();
    private final ObjectProperty<WeaponJfxModel> weapon = new SimpleObjectProperty<>();

    protected LicenseePresenceJfxModel(int id, LicenseeJfxModel licensee, LocalDateTime startDate, FiringPointJfxModel firingPoint) {
        this.id = id;
        this.licensee = licensee;
        this.startDate.set(startDate);
        this.firingPoint.set(firingPoint);
    }

    public int getId() {
        return id;
    }

    public LicenseeJfxModel getLicensee() {
        return licensee;
    }

    public LocalDateTime getStartDate() {
        return startDate.get();
    }

    public ReadOnlyObjectProperty<LocalDateTime> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate.set(startDate);
    }

    public LocalDateTime getStopDate() {
        return stopDate.get();
    }

    public ReadOnlyObjectProperty<LocalDateTime> stopDateProperty() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate.set(stopDate);
    }

    public FiringPointJfxModel getFiringPoint() {
        return firingPoint.get();
    }

    public ReadOnlyObjectProperty<FiringPointJfxModel> firingPointProperty() {
        return firingPoint;
    }

    public void setFiringPoint(FiringPointJfxModel firingPoint) {
        this.firingPoint.set(firingPoint);
    }

    public FiringPostJfxModel getFiringPost() {
        return firingPost.get();
    }

    public ReadOnlyObjectProperty<FiringPostJfxModel> firingPostProperty() {
        return firingPost;
    }

    public void setFiringPost(FiringPostJfxModel firingPost) {
        this.firingPost.set(firingPost);
    }

    public WeaponJfxModel getWeapon() {
        return weapon.get();
    }

    public ReadOnlyObjectProperty<WeaponJfxModel> weaponProperty() {
        return weapon;
    }

    public void setWeapon(WeaponJfxModel weapon) {
        this.weapon.set(weapon);
    }
}
