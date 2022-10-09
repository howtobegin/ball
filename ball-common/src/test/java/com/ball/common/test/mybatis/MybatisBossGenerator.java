package com.ball.common.test.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author littlehow
 */
public class MybatisBossGenerator {
    private static void run(String parentModule, String dbName, String pkgName, String[] tableNames) {
        //1. 全局配置
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath: " + projectPath);

        String moduleName = parentModule + "-boss";

        GlobalConfig globalConfig = new GlobalConfig()
                .setOutputDir(projectPath + "/" + moduleName + "/src/main/java")
                .setAuthor("littlehow")
                .setOpen(false)
                ;

        //2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://172.29.18.245:3306/" + dbName + "?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("hnt_user")
                .setPassword("wmt18iYJCekG91eQ")
                ;

        //3. 包名策略配置
        PackageConfig pkConfig = new PackageConfig()
                .setEntity("dao.entity")
                .setMapper("dao.mapper")
                .setXml("dao.mapper.xml")
                .setParent(pkgName);

        //4. 策略配置
        StrategyConfig stConfig = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setControllerMappingHyphenStyle(true);

        //5. 整合配置
        AutoGenerator mpg = new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(stConfig)
                .setTemplate(new TemplateConfig().setController(null))
                .setPackageInfo(pkConfig);
        mpg.execute();
    }

    public static void main(String[] args) {
        //模块名称
        String parentModule = "ball";
        //数据库名称
        String dbName = "ball";
        //包名
        String pkgName = "com.ball.boss";
        //表
        String[] tablesName = {"boss_check_log","boss_code","boss_kick_out_log" ,"boss_login_log", "boss_login_session",
                "boss_menu","boss_operation_log","boss_role", "boss_role_authorize","boss_user_authorize",
                "boss_user_info","boss_user_lock_info", "ip_address"};
        run(parentModule, dbName, pkgName, tablesName);
    }
}
