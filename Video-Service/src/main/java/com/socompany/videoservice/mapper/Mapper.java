package com.socompany.videoservice.mapper;

public interface Mapper<F, T> {
    T map(F toObject);

    default T map(F fromObj, T toObj){
        throw new UnsupportedOperationException("Not implemented.");
    }
}
