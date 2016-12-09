package realm.demo.com.demorxandroid;

import rx.Observable;

/**
 * Created by ThinhDT on 12/8/16.
 * Copyright (c) 2016 Rai and Rohl Technologies, Inc. All rights reserved.
 */

public class SomeType {
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public Observable<String> valueObservable() {
        return Observable.just(value);
    }
}