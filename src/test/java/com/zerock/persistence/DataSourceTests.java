package com.zerock.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j;
import testConfig.ApplicationContextTest;

@Log4j
public class DataSourceTests extends ApplicationContextTest{
    // DataSouce를 얻어와서, DB 커넥션을 테스트한다.
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
     
    @Test
    public void testConnection() {
        try(Connection con = dataSource.getConnection()){
            log.info(con);
        }catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testMyBatis() {
        try (SqlSession session = sqlSessionFactory.openSession();
                Connection con = session.getConnection();) {
            log.info("mybatis session >> "+ session);
            log.info("mybatis con >> "+ con);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
