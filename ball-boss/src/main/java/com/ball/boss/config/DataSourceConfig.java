package com.ball.boss.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ball.base.transaction.TransactionSupport;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Slf4j
@Setter
@Configuration
public class DataSourceConfig {

    @Autowired
    private DbConfig dbConfig;

    @Primary
    @Bean(name = "myDataSource")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dbConfig, druidDataSource);
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "txRequired")
    public TransactionTemplate txRequired() {
        return new TransactionTemplate(transactionManager());
    }

    @Bean(name = "txReadCommit")
    public TransactionTemplate txReadCommit() {
        TransactionTemplate template = new TransactionTemplate(transactionManager());
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionSupport transactionSupport() {
        return new TransactionSupport(txRequired(), txReadCommit());
    }
}
