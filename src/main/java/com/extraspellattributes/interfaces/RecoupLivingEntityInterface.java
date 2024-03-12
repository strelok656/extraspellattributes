package com.extraspellattributes.interfaces;

import com.extraspellattributes.api.RecoupInstance;

import java.util.List;

public interface RecoupLivingEntityInterface {
    List<RecoupInstance> getRecoups();
    void tickRecoups();
    void addRecoup(RecoupInstance instance);
}
