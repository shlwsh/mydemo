package com.dao;

import com.winning.sx.common.dao.mongodb.MongoDBDao;
import org.junit.Test;

/**
 * Created by smq on 16/7/1.
 */
public class MongoDBDaoTest {
    @Test
    public void mongoDBClientTest() throws Exception {

    }

    @Test
    public void testQuery() throws Exception {
        new MongoDBDao().testQuery();
    }

    @Test
    public void mgQuery() throws Exception {

    }

    @Test
    public void testInsert() throws Exception {
        new MongoDBDao().testInsert();
    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testToJsonObject() throws Exception {

    }

}