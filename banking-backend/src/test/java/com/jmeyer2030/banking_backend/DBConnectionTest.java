package com.jmeyer2030.banking_backend;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DBConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws Exception {
        assertThat(dataSource).isNotNull();
        assertThat(dataSource.getConnection()).isNotNull();
    }
}
