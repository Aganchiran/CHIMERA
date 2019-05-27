package com.aganchiran.chimera.viewmodels;

public interface ItemVM<M> {

    void insert(M itemModel);

    void update(M itemModel);

    void delete(M itemModel);
}
