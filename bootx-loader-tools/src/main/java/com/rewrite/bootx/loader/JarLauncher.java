package com.rewrite.bootx.loader;

import java.net.URL;

/**
 * @ClassName JarLauncher
 * @Decription
 * @Author wangjing
 * @Date 22.7.11 0011 16:55
 **/
public class JarLauncher extends org.springframework.boot.loader.JarLauncher {
    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new CachedLaunchedURLClassLoader(urls, getClass().getClassLoader(),getArchive());
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        new JarLauncher().launch(args);
        System.out.println(String.format("started time:%s s", (System.currentTimeMillis()-start)/1000));
    }
}

