package test;

import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author lemon
 * @Date 2022/9/15 9:57
 * @Version 1.0
 */
public class Testtt {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("static/test.txt");
        InputStream inputStream = classPathResource.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buf = new byte[1024];
        while((len=inputStream.read(buf))!=-1){
            baos.write(buf,0,len);
        }
        inputStream.close();
        System.out.println(new String(baos.toByteArray()));
    }
}
