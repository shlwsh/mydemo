package com.sqlserver;

import com.winning.sx.common.dao.sqlserver.sqldao;
import org.junit.Test;

/**
 * Created by smq on 16/7/1.
 */
public class sqldaoTest {
    @Test
    public void sqlRun() throws Exception {
        new sqldao().sqlRun();
    }

}