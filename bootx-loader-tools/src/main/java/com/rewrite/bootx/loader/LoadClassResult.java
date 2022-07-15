package com.rewrite.bootx.loader;

/**
 * @ClassName LoadClassResult
 * @Decription
 * @Author wangjing
 * @Date 22.7.11 0011 17:50
 **/
public class LoadClassResult {
    public static LoadClassResult NOT_FOUND = new LoadClassResult(null,null);
    private Class<?> clazz;
    private ClassNotFoundException ex;

    public LoadClassResult(ClassNotFoundException ex, Class<?> clazz) {
        this.ex = ex;
        this.clazz = clazz;
    }

    public ClassNotFoundException getEx() {
        return ex;
    }

    public void setEx(ClassNotFoundException ex) {
        this.ex = ex;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
