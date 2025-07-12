package com.guilhermescherer.msservicewatch.converter.request;

public interface UpdateConverter<S, T> {

    T convert(S source, T target);
}
