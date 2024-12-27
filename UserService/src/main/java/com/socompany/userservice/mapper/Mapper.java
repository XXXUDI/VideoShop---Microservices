package com.socompany.userservice.mapper;

public interface Mapper<F, T> {
    T map(F fromObject);

    default T map(F fromObj, T toObj){
        throw new UnsupportedOperationException("Not implemented.");
    }
}
