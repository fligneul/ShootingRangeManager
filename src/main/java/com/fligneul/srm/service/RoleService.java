package com.fligneul.srm.service;

import com.fligneul.srm.ui.model.user.ERole;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class RoleService {
    private final Subject<ERole> roleSubject = BehaviorSubject.createDefault(ERole.NONE);

    public void send(final ERole userRole) {
        roleSubject.onNext(userRole);
    }

    public Observable<ERole> getRoleObs() {
        return roleSubject;
    }
}
