package com.rewrite.bootx.loader;

import org.springframework.boot.loader.LaunchedURLClassLoader;
import org.springframework.boot.loader.archive.Archive;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName JarIndexLaunchedURLClassLoader
 * @Decription
 * @Author wangjing
 * @Date 22.7.11 0011 17:48
 **/

public class JarIndexLaunchedURLClassLoader extends LaunchedURLClassLoader {
    Archive rootArchive;
    JarFileResourceLoader jarFileResourceLoader;
    public JarIndexLaunchedURLClassLoader(boolean exploded, Archive rootArchive, URL[] urls, ClassLoader parent) {
        super(exploded, urls, parent);
        this.rootArchive = rootArchive;
        initJarIndex(urls); // 根据 INDEX.LIST 创建包名到 jar 文件的映射关系
    }

    private void initJarIndex(URL[] urls) {
        Map<String, URL> classMap;
        Map<String, List<Map<String, URL>>> classStringMap = new HashMap<>(5000);
        for (URL url : urls) {
            try {
                JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                List<Map<String, URL>> urlmaps;
                for(Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements(); ) {
                    JarEntry jarEntry = enumeration.nextElement();
                    if(jarEntry.getName().endsWith(".class")){

                        int i = jarEntry.getName().lastIndexOf("/");
                        String packages;
                        if(i>0){
                            packages = jarEntry.getName().substring(0, i);
                        }else {
                            packages = jarEntry.getName();
                        }

                        if(!classStringMap.containsKey(packages)){
                            urlmaps = new ArrayList<>(5);
                            classMap = new HashMap<>(500);
                            urlmaps.add(classMap);
                            classStringMap.put(packages,urlmaps);
                            classMap.put(jarEntry.getName(),new URL(((org.springframework.boot.loader.jar.JarFile)jarFile).getUrl(), jarEntry.getName()));
                        }else {
                            List<Map<String, URL>> lists = classStringMap.get(packages);
                            int size = lists.size();
                            for (int i1 = 0; i1 < size; i1++) {
                                Map<String, URL> maps = lists.get(i1);
                                if(!maps.containsKey(jarEntry.getName())){
                                    maps.put(jarEntry.getName(),new URL(((org.springframework.boot.loader.jar.JarFile)jarFile).getUrl(), jarEntry.getName()));
                                }else {
                                    Map<String, URL> classMap1 = new HashMap<>(4);
                                    classMap1.put(jarEntry.getName(),new URL(((org.springframework.boot.loader.jar.JarFile)jarFile).getUrl(), jarEntry.getName()));
                                    lists.add(classMap1);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jarFileResourceLoader = new JarFileResourceLoader();
        jarFileResourceLoader.setClassMap(classStringMap);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) return loadedClass;

        if (name.startsWith("org.springframework.boot.loader.jarmode.")) {
            return super.loadClass(name, resolve);
        }
        // skip java.*, org.w3c.dom.* com.sun.* ，这些包交给 java 默认的 classloader 去处理
           if (!name.startsWith("java") && !name.contains("org.w3c.dom.")
                    && !name.contains("xml") && !name.startsWith("com.sun")) {
                int lastDot = name.lastIndexOf('.');
                if (lastDot >= 0) {
                    String packageName = name.substring(0, lastDot);
                    String packageEntryName = packageName.replace('.', '/');
                    String path = name.replace('.', '/').concat(".class");

                    ClassSpec classSpec = jarFileResourceLoader.getClassSpec(path,packageEntryName);
                    if (Objects.nonNull(classSpec)) {
                        Class<?> definedClass = defineClass(name, classSpec.getBytes(), 0, classSpec.getBytes().length, classSpec.getCodeSource());
                        definePackageIfNecessary(name);
                        return definedClass;
                    }
                }
            }
        // 执行到这里，说明需要父类加载器来加载类（兜底）
        definePackageIfNecessary(name);
        return super.loadClass(name, resolve);
    }

    private void definePackageIfNecessary(String className) {
        int lastDot = className.lastIndexOf(46);
        if (lastDot >= 0) {
            String packageName = className.substring(0, lastDot);
            if (this.getPackage(packageName) == null) {
                try {
                    this.definePackage(className, packageName);
                } catch (IllegalArgumentException var5) {
                    if (this.getPackage(packageName) == null) {
                        throw new AssertionError("Package " + packageName + " has already been defined but it could not be found");
                    }
                }
            }
        }

    }

    private void definePackage(String className, String packageName) {
        String packageEntryName = packageName.replace('.', '/') + "/";
        String classEntryName = className.replace('.', '/') + ".class";
        URL[] var5 = this.getURLs();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            URL url = var5[var7];

            try {
                URLConnection connection = url.openConnection();
                if (connection instanceof JarURLConnection) {
                    JarFile jarFile = ((JarURLConnection)connection).getJarFile();
                    if (jarFile.getEntry(classEntryName) != null && jarFile.getEntry(packageEntryName) != null && jarFile.getManifest() != null) {
                        this.definePackage(packageName, jarFile.getManifest(), url);
                        return ;
                    }
                }
            } catch (IOException var11) {
            }
        }

        return ;

    }
}