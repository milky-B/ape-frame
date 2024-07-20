package com.airport.ape.mbg.bin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;


public class MBGPlugin extends PluginAdapter {

    //dao 默认父类
    private static final String DEFAULT_DAO_SUPER_CLASS = "tk.mybatis.mapper.common.Mapper";
    //dao 目标文件夹 targetProject
    private String daoTargetDir;
    //dao 目标包 targetPackage
    private String daoTargetPackage;
    private Boolean generateMapper;

    //dao 父类
    private String daoSuperClass;
    //lombok 开关
    private String lombok;
    private Boolean hasLombok;


    /**
     * 验证参数是否有效
     *
     * @param warnings
     * @return
     */
    public boolean validate(List<String> warnings) {
        daoTargetDir = properties.getProperty("mapper.targetProject");
        daoTargetPackage = properties.getProperty("mapper.targetPackage");
        generateMapper=stringHasValue(daoTargetDir)&&stringHasValue(daoTargetPackage);

        daoSuperClass = properties.getProperty("daoSuperClass",DEFAULT_DAO_SUPER_CLASS);
        lombok = properties.getProperty("lombok","false");
        hasLombok = Boolean.parseBoolean(lombok);
        return true;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> generatedFiles = new ArrayList<GeneratedJavaFile>();
        if(generateMapper){
            generateForMapper(generatedFiles,javaFormatter,introspectedTable);
        }
        /*if(generateMapper){
            generateFoeModel(mapperJavaFiles,javaFormatter,introspectedTable);
        }*/

        FullyQualifiedJavaType baseModelJavaType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        // 1. 生成 Model 类
        TopLevelClass modelClass = new TopLevelClass(baseModelJavaType);

        if (hasLombok) {
            // 添加domain的import
            modelClass.addImportedType("lombok.Data");

            // 添加domain的注解
            modelClass.addAnnotation("@Data");
        }

        modelClass.addImportedType("javax.persistence.Table");
        modelClass.addAnnotation("@Table(name = \""+introspectedTable.getFullyQualifiedTableNameAtRuntime()+"\")");
        modelClass.addJavaDocLine("/**");

        String remarks = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        modelClass.addJavaDocLine(" * " + remarks+"Po");


        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        modelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @author ").append(properties.getProperty("user.name"));
        modelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @date ");
        sb.append(getDateString());
        modelClass.addJavaDocLine(sb.toString());
        modelClass.addJavaDocLine(" */");

        //给主键添加注解
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        for (IntrospectedColumn primaryKeyColumn : primaryKeyColumns) {
            // 获取主键字段的属性名
            String propertyName = primaryKeyColumn.getJavaProperty();

            // 在属性上添加@Id注解
            Field field = findField(modelClass, propertyName);
            if (field != null) {
                modelClass.addImportedType("javax.persistence.Id");
                field.addAnnotation("@Id");
                modelClass.addImportedType("javax.persistence.GeneratedValue");
                modelClass.addImportedType("javax.persistence.GenerationType");
                field.addAnnotation("@GeneratedValue(\n" +
                        "            strategy = GenerationType.IDENTITY\n" +
                        "    )");
            }
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            FullyQualifiedJavaType javaType = introspectedColumn.getFullyQualifiedJavaType();
            String propertyName = introspectedColumn.getJavaProperty();
            Field field = new Field(propertyName, javaType);
            field.addJavaDocLine("/**");
            remarks = introspectedColumn.getRemarks();
            if (StringUtility.stringHasValue(remarks)) {
                String[] remarkLines = remarks.split(System.getProperty("line.separator"));
                for (String remarkLine : remarkLines) {
                    field.addJavaDocLine(" * " + remarkLine);
                }
            }
            field.addJavaDocLine(" */");
            field.setVisibility(JavaVisibility.PRIVATE);
            modelClass.addField(field);
        }

        generatedFiles.add(new GeneratedJavaFile(modelClass, daoTargetDir, javaFormatter));
        return generatedFiles;
    }

    private void generateFoeModel(List<GeneratedJavaFile> mapperJavaFiles, JavaFormatter javaFormatter, IntrospectedTable introspectedTable) {

    }

    private void generateForMapper(List<GeneratedJavaFile> mapperJavaFiles, JavaFormatter javaFormatter, IntrospectedTable introspectedTable) {
        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();

            GeneratedJavaFile mapperJavafile = null;

            Interface mapperInterface = new Interface(daoTargetPackage + "." + shortName + "Mapper");

            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * "+introspectedTable.getFullyQualifiedTableNameAtRuntime()+"Dao");
            mapperInterface.addJavaDocLine(" * ");
            mapperInterface.addJavaDocLine(" * @author "+properties.getProperty("user.name"));
            mapperInterface.addJavaDocLine(" * @Date "+getDateString());
            mapperInterface.addJavaDocLine(" */");

            FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(daoSuperClass);
            // 添加泛型支持
            daoSuperType.addTypeArgument(baseModelJavaType);

            mapperInterface.addImportedType(baseModelJavaType);
            //mapperInterface.addImportedType(daoSuperType);
            mapperInterface.addSuperInterface(daoSuperType);

            mapperInterface.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
            mapperInterface.addAnnotation("@Mapper");

            mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFormatter);
            mapperJavaFiles.add(mapperJavafile);
        }
    }

    private Field findField(TopLevelClass topLevelClass, String propertyName) {
        // 查找属性
        for (Field field : topLevelClass.getFields()) {
            if (field.getName().equals(propertyName)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !hasLombok;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !hasLombok;
    }
    protected String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(properties.getProperty("dateTimeFormat", "yyyy-MM-dd HH:mm:ss"));
        return simpleDateFormat.format(new Date());
    }

}