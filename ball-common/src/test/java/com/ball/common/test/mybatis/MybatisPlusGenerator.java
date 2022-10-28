package com.ball.common.test.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author littlehow
 */
public class MybatisPlusGenerator {
    private static void run(String parentModule, String dbName, String pkgName, String[] tableNames, String packageName) {
        //1. 全局配置
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath: " + projectPath);

        String moduleName = parentModule + "-biz";

        GlobalConfig globalConfig = new GlobalConfig()
                .setOutputDir(projectPath + "/" + moduleName + "/src/main/java")
                .setAuthor("atom")
                .setOpen(false);

        //2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://e2c5589a96e946839af0dd460774ce84in01.internal.cn-south-1.mysql.rds.myhuaweicloud.com:3306/" + dbName + "?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("hnt_user")
                .setPassword("wmt18iYJCekG91eQ")
                ;

        if (packageName == null || packageName.length() == 0) {
            packageName = "";
        } else if (!packageName.endsWith(".")) {
            packageName = packageName + ".";
        }

        //3. 包名策略配置
        PackageConfig pkConfig = new PackageConfig()
                .setEntity(packageName + "entity")
                .setMapper(packageName + "mapper" )
                .setXml(packageName + "mapper.xml")
                .setService(packageName + "service")
                .setServiceImpl(packageName + "service.impl")
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
        String pkgName = "com.ball.biz";
        // 子包名(按模块分包)
        String packageName = "account";
        //表
        String[] tablesName = {"biz_asset_adjustment_order"};
        run(parentModule, dbName, pkgName, tablesName, packageName);
    }
}
