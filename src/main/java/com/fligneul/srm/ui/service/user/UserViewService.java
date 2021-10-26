package com.fligneul.srm.ui.service.user;

import com.fligneul.srm.service.RoleService;
import com.fligneul.srm.ui.model.user.ERole;
import com.fligneul.srm.ui.node.settings.items.AccountSettingsNode;
import com.fligneul.srm.ui.node.settings.items.FiringPointSettingsNode;
import com.fligneul.srm.ui.node.settings.items.GeneralSettingsNode;
import com.fligneul.srm.ui.node.settings.items.ISettingsItemNode;
import com.fligneul.srm.ui.node.settings.items.UsersSettingsNode;
import com.fligneul.srm.ui.node.settings.items.WeaponSettingsNode;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserViewService {

    private final Map<ERole, List<Class<? extends ISettingsItemNode>>> settingsViewByRoleMap = new HashMap<>();
    private final Subject<List<Class<? extends ISettingsItemNode>>> visibleSettings = BehaviorSubject.createDefault(List.of());

    public UserViewService() {
        settingsViewByRoleMap.put(ERole.ADMINISTRATOR,
                Arrays.asList(
                        GeneralSettingsNode.class,
                        FiringPointSettingsNode.class,
                        AccountSettingsNode.class,
                        UsersSettingsNode.class,
                        WeaponSettingsNode.class
                )
        );

        settingsViewByRoleMap.put(ERole.USER,
                List.of(
                )
        );
    }

    @Inject
    private void injectDependencies(final RoleService roleService) {
        roleService.getRoleObs()
                .distinctUntilChanged()
                .subscribe(role -> visibleSettings.onNext(settingsViewByRoleMap.getOrDefault(role, List.of())));
    }

    public Subject<List<Class<? extends ISettingsItemNode>>> getAccessibleSettingsNode() {
        return visibleSettings;
    }

}