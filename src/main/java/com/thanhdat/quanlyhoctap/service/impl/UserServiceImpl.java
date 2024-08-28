package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Integer CURRENT_USER_LOGGED_ID = 26;


    @Override
    public Integer getCurrentUserId() {
        return CURRENT_USER_LOGGED_ID;
    }
}
