package com.rdesouter.dao.repository;

import com.rdesouter.model.SyncMessage;

public interface MessageRepoCustom {

    SyncMessage findById(String id);
}
