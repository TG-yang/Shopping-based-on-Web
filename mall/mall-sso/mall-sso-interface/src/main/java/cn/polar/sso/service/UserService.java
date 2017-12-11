package cn.polar.sso.service;

import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbUser;

/**
 * Description : TODO
 * Created By Polar on 2017/8/17
 */
public interface UserService {
    MallResult checkData(String param, Integer type);
    MallResult addUser(TbUser user);
    MallResult login(String username, String password);
    /**
     * 通过token取缓存中用户的信息，判断用户是否登录
     * @param token
     * @return
     */
    MallResult getUserByToken(String token);

    void deleteUserByToken(String token);
}
