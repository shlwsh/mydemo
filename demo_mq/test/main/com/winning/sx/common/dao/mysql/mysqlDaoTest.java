package com.winning.sx.common.dao.mysql;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by smq on 16/7/23.
 * new 001 modify
 * new 002 modify
 */
public class mysqlDaoTest {
    @Test
    public void runSql() throws Exception {
        mysqlDao d = new mysqlDao();
        d.connSQL();
        d.runSql();
    }

}