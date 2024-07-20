### mybatis-generator plugin demo

#### xml sample
1. 添加插件com.example.adapter.MapperPlugin
2. 配置属性
   - user.name 作者名
   - lombok 是否添加@Data
   - mapper.targetPackage 生成mapper所在包名
   - mapper.targetProject 生成mapper所在文件夹
   - daoSuperClass 父类,默认 tk.mybatis.mapper.common.Mapper 设置为空则不生成
   - daoExtendSuperClass 扩展父类,可配置多个,匹配前缀 daoExtendSuperClass
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>

    <!-- 数据库连接信息 -->
    <context id="MyBatisGenerator" targetRuntime="MyBatis3">


        <!--防止生成重复代码-->
        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin"></plugin>

        <plugin type="com.example.adapter.MapperPlugin">
            <property name="user.name" value="lee"/>
            <property name="lombok" value="true"/>
            <property name="mapper.targetPackage" value="com.example.mapper"/>
            <property name="mapper.targetProject" value="src/main/java"/>
            <property name="daoSuperClass" value=""/>
            <property name="daoExtendSuperClass1" value="tk.mybatis.mapper.common.MySqlMapper1"/>
            <property name="daoExtendSuperClass2" value="tk.mybatis.mapper.common.MySqlMapper2"/>
            <property name="daoExtendSuperClass3" value="tk.mybatis.mapper.common.MySqlMapper3"/>
        </plugin>

        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!--连接数据库信息-->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/white_list"
                        userId="root"
                        password="root">
        </jdbcConnection>

        <!-- 实体类生成的目标包配置 -->
        <javaModelGenerator targetPackage="com.example.model" targetProject="src/main/java"></javaModelGenerator>

        <!-- 表的映射配置 -->
        <table tableName="user" domainObjectName="User"/>
        <!-- 可以添加多个table节点配置多个表 -->
        <table tableName="goods" domainObjectName="Goods"/>
        <table tableName="role" domainObjectName="Role"/>

    </context>

</generatorConfiguration>

```

#### use demo
1. 导入依赖
2. 编辑xml
3. 传递xml所在路径调用Generator.runner
```java
import com.example.Generator;

import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        String filePath = System.getProperty("user.dir") + File.separator + "generator"+File.separator+"mybatisGeneratorConfig.xml";
        System.out.println(filePath);
        Generator.runner(filePath);
    }
}

```