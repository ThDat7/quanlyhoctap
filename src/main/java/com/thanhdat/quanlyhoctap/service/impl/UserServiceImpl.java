package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    private static final Long CURRENT_USER_LOGGED_ID = (long) 26;


    @Override
    public Long getCurrentUserId() {
        return CURRENT_USER_LOGGED_ID;
    }
}
