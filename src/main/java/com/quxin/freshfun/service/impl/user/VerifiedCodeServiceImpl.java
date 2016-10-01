package com.quxin.freshfun.service.impl.user;

import com.quxin.freshfun.dao.UsersMapper;
import com.quxin.freshfun.service.user.VerifiedCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qucheng on 2016/10/1.
 */
@Service("verifiedCodeService")
public class VerifiedCodeServiceImpl implements VerifiedCodeService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Integer removeVerifyCode() {
        return usersMapper.deleteVerifyCode();
    }
}
