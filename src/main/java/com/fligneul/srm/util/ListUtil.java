package com.fligneul.srm.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ListUtil {

    public static <T> Optional<Integer> getIndex(List<T> list, Predicate<T> predicate) {
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
