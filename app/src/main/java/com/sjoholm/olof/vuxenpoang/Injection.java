package com.sjoholm.olof.vuxenpoang;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.storage.LocalRepository;
import com.sjoholm.olof.vuxenpoang.storage.Repository;

public class Injection {

    public static Repository getRepository(@NonNull Context context) {
        return new LocalRepository(context);
    }
}
