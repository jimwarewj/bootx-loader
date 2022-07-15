package com.rewrite.bootx.loader;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName JarFileResourceLoader
 * @Decription
 * @Author wangjing
 * @Date 22.7.13 0013 14:22
 **/
public class JarFileResourceLoader {
    private Map<String, List<Map<String, URL>>> classMap;
    public JarFileResourceLoader(){
    }

    public void setClassMap(Map<String, List<Map<String, URL>>> classMap){
        this.classMap = classMap;
    }

    public ClassSpec getClassSpec(String path,String packageEntryName){
        List<Map<String, URL>> urls = classMap.get(packageEntryName);
        if(Objects.nonNull(urls)){
            for (Map<String, URL> url : urls) {
                if(url.containsKey(path)){
                    return new ClassSpec(url.get(path),path);
                }
            }
        }
        return null;
    }
}
