package airport.cargos.com.spring.starter.handler;

import airport.cargos.com.spring.starter.bean.UserOptions;
import airport.cargos.com.spring.starter.conf.UserProperty;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public abstract class AbstractGeneratorHandler<T> {
    @Autowired
    private UserProperty userProperty;
    private static String tempDir = "temp";

    //todo 多余的IO操作,应该直接输出数据到压缩文件,不需要先生成文件夹再压缩
    //todo 使用信息队列定时清理文件
    public T start(UserOptions userOptions) throws ServiceException, IOException {
        // 生成项目文件
        String filePath = generateProjectFiles(userOptions);
        // 创建压缩文件
        String zipFilePath = zipCompress(filePath);
        // 将压缩文件直接返回给用户
        return returnZipToUser(zipFilePath);
    }

    protected String generateProjectFiles(UserOptions userOptions) throws FileNotFoundException {
        // 获取项目根目录
        String projectRoot = System.getProperty("user.dir") + File.separator + tempDir;

        String project = "project_" + System.currentTimeMillis();
        String src = projectRoot + File.separator + project + File.separator + "src";
        // 生成src目录及其子目录
        generateDirectory(projectRoot, project);
        generateDirectory(projectRoot + File.separator + project, "src");
        generateDirectory(src, "main");
        generateDirectory(src, "test");
        generateDirectory(src + File.separator + "main", "java");
        generateDirectory(src + File.separator + "main", "resources");

        String packPath = src + File.separator + "main" + File.separator + "java" +
                File.separator + userOptions.getPackaging().replace(".", File.separator);
        //生成package
        //生成基本包
        generateDirectory(packPath, "controller");
        generateDirectory(packPath, "service");
        generateDirectory(packPath, "mapper");
        generateDirectory(packPath, "bean");
        generateDirectory(packPath, "conf");
        generateDirectory(packPath, "base");
        generateDirectory(packPath, "constant");
        // 生成Application.Java文件
        generateResourceFile(packPath, "Application.java", generateApplicationContent(userOptions.getPackaging()));

        // 生成application.yml文件
        generateResourceFile(src + File.separator + "main" + File.separator + "resources",
                "application.yml", generateYamlContent(userOptions));
        generateResourceFile(src, "pom.xml", generatePomContent(userOptions));
        return projectRoot + File.separator + project;
    }

    private void generateDirectory(String parentPath, String directoryName) {
        File directory = new File(parentPath + File.separator + directoryName);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("directory generated successfully: {}", directory.getAbsolutePath());
            }
        }
    }

    private void generateResourceFile(String parentPath, String fileName, String content) throws FileNotFoundException {
        String filePath = parentPath + File.separator + fileName;
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(StringUtils.hasLength(content) ? content : "empty");
            log.info("file generated successfully: {}", filePath);
        }
    }

    private String generateApplicationContent(String packageName) {
        // 生成 Application.java 文件的内容，这里简单示例一下
        return "package " + packageName + ";\n\n" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                "@SpringBootApplication" +
                "public class Application {\n" +
                "    public static void main(String[] args) {\n" +
                "        //配置异步日志上下文选择器,不配置的话，等于没开异步\n" +
                "        System.setProperty(\"log4jContextSelector\",\"org.apache.logging.log4j.core.async.AsyncLoggerContextSelector\");\n" +
                "        SpringApplication.run(Application.class);\n" +
                "    }\n" +
                "}\n";
    }

    private String generateYamlContent(UserOptions userOptions) {
        // 生成 application.yml 文件的内容，这里简单示例一下
        return "server:\n" +
                "  port: 8081" +
                "spring:\n" +
                "  application:\n" +
                "    name: " + userOptions.getApplication() + "\n";
    }

    private String generatePomContent(UserOptions userOptions) {
        String pom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n";
        pom += appendParent(userOptions);
        pom += "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>" + userOptions.getGroupId() + "</groupId>\n" +
                "    <artifactId>" + userOptions.getArtifactId() + "</artifactId>\n" +
                "    <version>" + userOptions.getVersion() + "</version>\n";
        pom += StringUtils.hasLength(userOptions.getName()) ? "    <name>" + userOptions.getName() + "</name>" : "";
        pom += StringUtils.hasLength(userOptions.getDescription()) ? "    <description>" + userOptions.getDescription() + "</description>" : "";
        pom += "\n" +
                "    <properties>\n" +
                "        <java.version>1.8</java.version>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <maven.compiler.source>8</maven.compiler.source>\n" +
                "        <maven.compiler.target>8</maven.compiler.target>\n" +
                "        <maven.plugin.version>3.8.0</maven.plugin.version>\n" +
                "    </properties>\n" +
                "\n";
        pom += appendDependency(userOptions);
        pom += "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.springframework.boot</groupId>\n" +
                "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                "                <version>2.7.17</version>\n" +
                "                <configuration>\n" +
                "                    <mainClass>\n" +
                "                        " + userOptions.getPackaging() + ".Application\n" +
                "                    </mainClass>\n" +
                "                </configuration>\n" +
                "                <executions>\n" +
                "                    <execution>\n" +
                "                        <goals>\n" +
                "                            <goal>repackage</goal>\n" +
                "                        </goals>\n" +
                "                    </execution>\n" +
                "                </executions>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>${maven.plugin.version}</version>\n" +
                "                <configuration>\n" +
                "                    <source>${maven.compiler.source}</source>\n" +
                "                    <target>${maven.compiler.target}</target>\n" +
                "                    <encoding>UTF-8</encoding>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "        <resources>\n" +
                "            <resource>\n" +
                "                <directory>src/main/resources</directory>\n" +
                "                <filtering>true</filtering>\n" +
                "            </resource>\n" +
                "            <resource>\n" +
                "                <directory>src/main/java</directory>\n" +
                "                <includes>\n" +
                "                    <include>**/*.xml</include>\n" +
                "                </includes>\n" +
                "            </resource>\n" +
                "        </resources>\n" +
                "    </build>" +
                "</project>";
        return pom;
    }

    public String appendDependency(UserOptions userOptions) {
        List<JSONObject> dependencyList = dependencies(userOptions);
        String dependencies = "    <dependencies>\n";
        for (JSONObject dependency : dependencyList) {
            String version = dependency.getString("version");
            dependencies += "        <dependency>\n" +
                    "            <groupId>" + dependency.getString("groupId") + "</groupId>\n" +
                    "            <artifactId>" + dependency.getString("artifactId") + "</artifactId>\n";
            dependencies += StringUtils.hasLength(version) ? "            <version>" + version + "</version>" : "" +
                    "        </dependency>\n";
        }
        dependencies += "    </dependencies>";
        return dependencies;
    }

    public String appendParent(UserOptions userOptions) {
        JSONObject parent = parent(userOptions);
        if (parent == null) {
            return "";
        }
        String parentStr = "    <parent>\n" +
                "        <artifactId>" + parent.getString("artifactId") + "</artifactId>\n" +
                "        <groupId>" + parent.getString("groupId") + "</groupId>\n" +
                "        <version>" + parent.getString("version") + "</version>\n" +
                "    </parent>";
        return parentStr;
    }

    //包含业务逻辑 设置为抽象方法
    public abstract List<JSONObject> dependencies(UserOptions userOptions);

    public abstract JSONObject parent(UserOptions userOptions);

    // 在实际应用中，你需要根据你的服务框架和需求将压缩文件返回给用户
    // 例如，如果是Web应用，可以使用 HttpServletResponse 输出文件内容
    protected abstract T returnZipToUser(String file) throws IOException;

    public static String zipCompress(String project) throws ServiceException, IOException {
        String hallFilePath = project;
        int i = hallFilePath.lastIndexOf(".");
        hallFilePath = i == -1 ? hallFilePath : hallFilePath.substring(0, i);
        compress(Paths.get(hallFilePath).toString(), hallFilePath + ".zip");
        log.info("zip generated successfully: {}", hallFilePath + ".zip");
        return hallFilePath + ".zip";
    }

    public static void compress(String fromPath, String toPath) throws IOException, ServiceException {
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        if (!fromFile.exists()) {
            throw new ServiceException(fromPath + "不存在！");
        }
        try (FileOutputStream outputStream = new FileOutputStream(toFile);
             CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32());
             ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream)) {
            String baseDir = "";
            compress(fromFile, zipOutputStream, baseDir);
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (StringUtils.hasLength(baseDir)) {
            baseDir = baseDir.substring(baseDir.indexOf(File.separator), baseDir.length());
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte[] data = new byte[1024];
            while ((count = bis.read(data)) != -1) {
                zipOut.write(data, 0, count);
            }
        }
    }

    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        if (ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                compress(file, zipOut, baseDir + dir.getName() + File.separator);
            }
        }
    }

}
