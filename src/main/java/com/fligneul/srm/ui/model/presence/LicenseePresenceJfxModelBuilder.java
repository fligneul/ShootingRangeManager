package com.fligneul.srm.ui.model.presence;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;

import java.time.LocalDateTime;
import java.util.Optional;

public class LicenseePresenceJfxModelBuilder {
    private Integer id;
    private LicenseeJfxModel licensee;
    private LocalDateTime startDate;
    private LocalDateTime stopDate;
    private FiringPointJfxModel firingPoint;
    private FiringPostJfxModel firingPost;
    private WeaponJfxModel weapon;


    public LicenseePresenceJfxModelBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public LicenseePresenceJfxModelBuilder setLicensee(LicenseeJfxModel licensee) {
        this.licensee = licensee;
        return this;
    }

    public LicenseePresenceJfxModelBuilder setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LicenseePresenceJfxModelBuilder setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
        return this;
    }

    public LicenseePresenceJfxModelBuilder setFiringPoint(FiringPointJfxModel firingPoint) {
        this.firingPoint = firingPoint;
        return this;
    }

    public LicenseePresenceJfxModelBuilder setFiringPost(FiringPostJfxModel firingPost) {
        this.firingPost = firingPost;
        return this;
    }

    public LicenseePresenceJfxModelBuilder setWeapon(WeaponJfxModel weapon) {
        this.weapon = weapon;
        return this;
    }

    public LicenseePresenceJfxModel createLicenseePresenceJfxModel() {
        LicenseePresenceJfxModel model = new LicenseePresenceJfxModel(Optional.ofNullable(id).orElse(LicenseePresenceJfxModel.DEFAULT_ID), licensee, startDate, firingPoint);

        Optional.ofNullable(stopDate).ifPresent(model::setStopDate);
        Optional.ofNullable(firingPost).ifPresent(model::setFiringPost);
        Optional.ofNullable(weapon).ifPresent(model::setWeapon);

        return model;
    }
}
