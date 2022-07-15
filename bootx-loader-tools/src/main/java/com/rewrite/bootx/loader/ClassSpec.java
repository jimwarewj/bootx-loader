package com.rewrite.bootx.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
/**
 * @ClassName ClassSpec
 * @Decription
 * @Author wangjing
 * @Date 22.7.13 0013 13:57
 **/
public class ClassSpec {
    byte[] data;
    String name;
    URL url;
    public ClassSpec(URL url, String name){
        this.url = url;
        this.name = name;
        try (InputStream inputStream = url.openStream()){
            this.data = copy(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] copy(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        long count;
        int n;
        for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
    public byte[] getBytes(){
        return this.data;
    }

    public String getName() {
        return name;
    }

    public CodeSource getCodeSource(){
        CodeSource cs = new CodeSource(url, (CodeSigner[]) null);
        return cs;
    }
}


