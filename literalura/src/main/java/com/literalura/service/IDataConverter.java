package com.literalura.service;
//CREADA EN EL VIDEO 1.4
public interface IDataConverter {
    <T> T convertData(String json, Class<T> clase);
}
