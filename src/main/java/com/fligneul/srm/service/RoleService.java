package com.fligneul.srm.service;

import com.fligneul.srm.ui.model.user.ERole;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * User role service
 */
public class RoleService {
    private final Subject<ERole> roleSubject = BehaviorSubject.createDefault(ERole.NONE);

    /**
     * Publish the current user role
     *
     * @param userRole
     *         the current user role
     */
    public void publish(final ERole userRole) {
        roleSubject.onNext(userRole);
    }

    /**
     * @return an observable of the current user role
     */
    public Observable<ERole> getRoleObs() {
        return roleSubject;
    }
}
