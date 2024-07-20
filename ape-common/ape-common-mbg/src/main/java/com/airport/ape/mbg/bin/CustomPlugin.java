package com.airport.ape.mbg.bin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;


public class CustomPlugin extends PluginAdapter {

    private static final String DEFAULT_DAO_SUPER_CLASS = "tk.mybatis.mapper.common.Mapper";

    private String daoSuperClass;

    private ShellCallback shellCallback = null;

    public CustomPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> list) {
        daoSuperClass = properties.getProperty("daoSuperClass");
        if (!stringHasValue(daoSuperClass)) {
            daoSuperClass = DEFAULT_DAO_SUPER_CLASS;
        }
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        boolean hasLombok = Boolean.parseBoolean(properties.getProperty("hasLombok", "false"));
        if (hasLombok) {
            // 添加domain的import
            topLevelClass.addImportedType("lombok.Data");

            // 添加domain的注解
            topLevelClass.addAnnotation("@Data");
        }

        topLevelClass.addImportedType("javax.persistence.Table");
        topLevelClass.addAnnotation("@Table(name = \""+introspectedTable.getFullyQualifiedTableNameAtRuntime()+"\")");

        topLevelClass.addJavaDocLine("/**");

        String remarks = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        topLevelClass.addJavaDocLine(" * " + remarks+"Po");


        StringBuilder sb = new StringBuilder();
//        sb.append(" * ").append(introspectedTable.getFullyQualifiedTable());
        sb.append(" * ");
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @author ").append(properties.getProperty("user.name"));
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @date ");
        sb.append(getDateString());
        topLevelClass.addJavaDocLine(sb.toString());
        topLevelClass.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        field.addJavaDocLine("/**");
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }
        }
        field.addJavaDocLine(" */");
        return true;
    }

    /**
     * 生成 Mapper接口
     *
     * @param interfaze
     * @param introspectedTable
     * @return:boolean
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加Mapper的import
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));

        //添加Mapper接口注释
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTableNameAtRuntime()+"Dao");

        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        interfaze.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @author ").append(properties.getProperty("user.name"));
        interfaze.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @date ");
        sb.append(getDateString());
        interfaze.addJavaDocLine(sb.toString());
        interfaze.addJavaDocLine(" */");

        // 添加Mapper的注解
        interfaze.addAnnotation("@Mapper");
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    /**
     * 生成setter方法
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return:boolean
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 不生成getter
        boolean hasLombok = Boolean.parseBoolean(properties.getProperty("hasLombok", "false"));
        return !hasLombok;
    }

    /**
     * 生成getter方法
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @return:boolean
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 不生成setter
        boolean hasLombok = Boolean.parseBoolean(properties.getProperty("hasLombok", "false"));
        return !hasLombok;
    }

    protected String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(properties.getProperty("dateTimeFormat", "yyyy-MM-dd HH:mm:ss"));
        return simpleDateFormat.format(new Date());
    }



}
