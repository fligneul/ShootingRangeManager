package com.fligneul.srm.ui.service.user;

import com.fligneul.srm.ui.node.settings.items.AccountSettingsNode;
import com.fligneul.srm.ui.node.settings.items.ISettingsItemNode;
import com.fligneul.srm.ui.node.settings.items.UsersSettingsNode;

import java.util.Arrays;
import java.util.List;

public class UserViewService {

    public List<Class<? extends ISettingsItemNode>> getAccessibleSettingsNode() {
        return Arrays.asList(
                AccountSettingsNode.class,
                UsersSettingsNode.class
        );
    }

}
