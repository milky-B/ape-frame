import java.io.File;

public class TestMkDir {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\lee\\Desktop\\111\\111\\222"+File.separator+null);
        if(!file.exists()){
            if (file.mkdirs()) {
                System.out.println("successful");
            }
        }
    }
}
