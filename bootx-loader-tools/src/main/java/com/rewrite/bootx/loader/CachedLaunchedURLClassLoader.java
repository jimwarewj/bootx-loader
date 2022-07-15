package com.rewrite.bootx.loader;

import org.springframework.boot.loader.archive.Archive;

import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @ClassName CachedLaunchedURLClassLoader
 * @Decription
 * @Author wangjing
 * @Date 22.7.11 0011 16:57
 **/
public class CachedLaunchedURLClassLoader extends JarIndexLaunchedURLClassLoader {
    private final Map<String, LoadClassResult> classCache = new WeakHashMap<>(3000);
    public CachedLaunchedURLClassLoader(URL[] urls, ClassLoader parent, Archive archive) {
        super(false,archive,urls,parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClassWithCache(name, resolve);
    }
    private Class<?> loadClassWithCache(String name, boolean resolve) throws ClassNotFoundException {
        LoadClassResult result = classCache.get(name);
        if (result != null) {
            if (result.getEx() != null) {
                throw result.getEx();
            }
            return result.getClazz();
        }

        try {
            Class<?> clazz = super.findLoadedClass(name);
            if (clazz == null) {
                clazz = super.loadClass(name, resolve);
            }
            if (clazz == null) {
                classCache.put(name, LoadClassResult.NOT_FOUND);
            }
            classCache.put(name, new LoadClassResult(null,clazz));
            return clazz;
        } catch (ClassNotFoundException exception) {
            classCache.put(name, new LoadClassResult(exception,null));
            throw exception;
        }
    }

}
