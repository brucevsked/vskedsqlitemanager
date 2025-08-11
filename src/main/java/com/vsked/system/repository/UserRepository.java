package com.vsked.system.repository;

import com.vsked.system.domain.AccountName;
import com.vsked.system.domain.Page;
import com.vsked.system.domain.User;
import com.vsked.system.domain.UserId;

public interface UserRepository {

    void save(User user);
    Boolean isExistBy(AccountName accountName);
    Boolean isExistBy(UserId userId);
    User getBy(AccountName accountName);
    User getBy(UserId userId);
    Long nextUserId();

    Page<User> findAll(Page page);

}
