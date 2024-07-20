package com.airport.ape.mbg.adaptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;


public class MapperPlugin extends PluginAdapter {

    //dao 默认父类
    private static final String DEFAULT_DAO_SUPER_CLASS = "tk.mybatis.mapper.common.Mapper";
    //dao 目标文件夹 targetProject
    private String daoTargetDir;
    //dao 目标包 targetPackage
    private String daoTargetPackage;
    private Boolean generateMapper;

    //dao 父类
    private List<String> superClassList = new ArrayList<>();

    //lombok 开关
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

        String daoSuperClass = properties.getProperty("daoSuperClass",DEFAULT_DAO_SUPER_CLASS);
        if(stringHasValue(daoSuperClass)){
            superClassList.add(daoSuperClass);
        }

        superClassList = getPropertiesWithPrefix(properties, "daoExtendSuperClass");

        String lombok = properties.getProperty("lombok","false");
        hasLombok = Boolean.parseBoolean(lombok);
        return true;
    }
    private List<String> getPropertiesWithPrefix(Properties properties, String prefix) {
        List<String> result = new ArrayList<>();
        for (String propertyName : properties.stringPropertyNames()) {
            if (propertyName.startsWith(prefix)) {
                result.add(properties.getProperty(propertyName));
            }
        }
        return result;
    }

    /*
     * mapper
     * */
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
        if(!generateMapper){
            return mapperJavaFiles;
        }
        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();

            GeneratedJavaFile mapperJavaFile = null;

            Interface mapperInterface = new Interface(daoTargetPackage + "." + shortName + "Mapper");

            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * "+introspectedTable.getFullyQualifiedTableNameAtRuntime()+"Dao");
            mapperInterface.addJavaDocLine(" * ");
            mapperInterface.addJavaDocLine(" * @author "+properties.getProperty("user.name"));
            mapperInterface.addJavaDocLine(" * @Date "+getDateString());
            mapperInterface.addJavaDocLine(" */");

            superClassList.forEach(superClass->{
                FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(superClass);
                // 添加泛型支持
                daoSuperType.addTypeArgument(baseModelJavaType);
                mapperInterface.addImportedType(baseModelJavaType);
                //添加daoSuperClass,为默认时不能import,因为和@Mapper重名
                if(!daoSuperType.equals(DEFAULT_DAO_SUPER_CLASS)){
                    mapperInterface.addImportedType(daoSuperType);
                }
                mapperInterface.addSuperInterface(daoSuperType);
            });

            mapperInterface.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
            mapperInterface.addAnnotation("@Mapper");

            mapperJavaFile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFormatter);
            mapperJavaFiles.add(mapperJavaFile);
        }
        return mapperJavaFiles;
    }


    /*
     * 自定义model 注释,注解
     * */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
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

        //给主键添加注解
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        for (IntrospectedColumn primaryKeyColumn : primaryKeyColumns) {
            // 获取主键字段的属性名
            String propertyName = primaryKeyColumn.getJavaProperty();

            // 在属性上添加@Id注解
            Field field = findField(topLevelClass, propertyName);
            if (field != null) {
                topLevelClass.addImportedType("javax.persistence.Id");
                field.addAnnotation("@Id");
                topLevelClass.addImportedType("javax.persistence.GeneratedValue");
                topLevelClass.addImportedType("javax.persistence.GenerationType");
                field.addAnnotation("@GeneratedValue(\n" +
                        "            strategy = GenerationType.IDENTITY\n" +
                        "    )");
            }
        }
        return true;
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