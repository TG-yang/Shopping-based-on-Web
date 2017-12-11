package cn.polar.sso.service.impl;

import cn.polar.common.jedis.JedisClient;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.mapper.TbUserMapper;
import cn.polar.pojo.TbUser;
import cn.polar.pojo.TbUserExample;
import cn.polar.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description : TODO
 * Created By Polar on 2017/8/17
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION}")
    private String SESSION;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    /**
     * 检查注册信息中用户名或者手机号是否已被注册
     *
     * @param param
     * @param type
     * @return
     */
    @Override
    public MallResult checkData(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        if (1 == type) {
            // username check
            criteria.andUsernameEqualTo(param);
        } else if (2 == type) {
            // check phone
            criteria.andPhoneEqualTo(param);
        } else if (3 == type) {
            // check email
            criteria.andEmailEqualTo(param);
        } else {
            return MallResult.build(400, "Illegal Params");
        }

        // execute query
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);

        if (tbUsers != null && tbUsers.size() > 0) {
            return MallResult.ok(false);
        }


        return MallResult.ok(true);
    }

    /**
     * 新增用户（注册）
     *
     * @param user
     * @return
     */
    @Override
    public MallResult addUser(TbUser user) {
        // check data
        if (StringUtils.isBlank(user.getUsername())) {
            return MallResult.build(400, "username is not null");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            return MallResult.build(400, "password is not null");
        }

        //校验数据是否可用
        MallResult result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return MallResult.build(400, "此用户名已经被使用");
        }
        //校验电话是否可以
        if (StringUtils.isNotBlank(user.getPhone())) {
            result = checkData(user.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return MallResult.build(400, "此手机号已经被使用");
            }
        }
        //校验email是否可用
        if (StringUtils.isNotBlank(user.getEmail())) {
            result = checkData(user.getEmail(), 3);
            if (!(boolean) result.getData()) {
                return MallResult.build(400, "此邮件地址已经被使用");
            }
        }
        // 密码加密处理
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        // 补全属性

        user.setCreated(new Date());
        user.setUpdated(new Date());

        tbUserMapper.insert(user);
        return MallResult.ok();
    }

    /**
     * user login
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public MallResult login(String username, String password) {

        // check username and password
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TbUser> users = tbUserMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            return MallResult.build(400, "username or password is wrong");
        }
        TbUser user = users.get(0);
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return MallResult.build(400, "username or password is wrong");
        }

        // login success generate a token(same as Jsessionid)
        String token = UUID.randomUUID().toString();
        user.setPassword(null);
        // put token and user info into redis
        jedisClient.set(SESSION + ":" + token, JsonUtils.objectToJson(user));
        // set expire time
        jedisClient.expire(SESSION + ":" + token, SESSION_EXPIRE);

        return MallResult.ok(token);
    }

    /**
     * 通过token取缓存中用户的信息，判断用户是否登录
     * @param token
     * @return
     */
    @Override
    public MallResult getUserByToken(String token) {
        // query user by token from redis
        String userJson = jedisClient.get(SESSION + ":" + token);

        // no user about this token
        if (StringUtils.isBlank(userJson)) {
            return MallResult.build(400, "user login has expired");
        }
        TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);
        // reset the expired time
        jedisClient.expire(SESSION + ":" + token, SESSION_EXPIRE);

        return MallResult.ok(user);
    }

    @Override
    public void deleteUserByToken(String token) {
        jedisClient.del(SESSION + ":" + token);

    }
}
