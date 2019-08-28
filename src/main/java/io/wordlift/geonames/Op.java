package io.wordlift.geonames;

public interface Op<T> {

    String getPath();

    T execute(byte[] bytes);

}