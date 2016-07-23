package com.wnsx.java;

import com.winning.sx.common.dao.redis.RedisOpDao;
import org.junit.Test;

/**
 * Created by smq on 16/7/1.
 */
public class RedisOpDaoTest {
    @Test
    public void show() throws Exception {

    }

    @Test
    public void stringOperate() throws Exception {
        new RedisOpDao().stringOperate();
    }

    @Test
    public void listOperate() throws Exception {
        new RedisOpDao().listOperate();
    }

    @Test
    public void setOperate() throws Exception {
        new RedisOpDao().setOperate();
    }

    @Test
    public void sortedSetOperate() throws Exception {
        new RedisOpDao().sortedSetOperate();
    }

    @Test
    public void hashOperate() throws Exception {
        new RedisOpDao().hashOperate();
    }

}