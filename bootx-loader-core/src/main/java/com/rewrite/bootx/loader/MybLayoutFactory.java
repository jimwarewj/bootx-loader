package com.rewrite.bootx.loader;

import org.springframework.boot.loader.tools.CustomLoaderLayout;
import org.springframework.boot.loader.tools.Layout;
import org.springframework.boot.loader.tools.LayoutFactory;
import org.springframework.boot.loader.tools.LibraryScope;
import org.springframework.boot.loader.tools.LoaderClassesWriter;
import org.springframework.boot.loader.tools.RepackagingLayout;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class MybLayoutFactory implements LayoutFactory
{
    private static final String NESTED_LOADER_JAR = "META-INF/loader/spring-boot-loader.jar";
    private static final String NESTED_LOADER_JAR_BOOTX = "META-INF/loader/bootx-loader-tools.jar";

    @Override
    public Layout getLayout(File source) {
        return new Jar();
    }
    public static class Jar implements RepackagingLayout, CustomLoaderLayout {
        @Override
        public void writeLoadedClasses(LoaderClassesWriter writer) throws IOException {
            // 拷贝 springboot loader 相关的文件到 jar 根目录
            writer.writeLoaderClasses(NESTED_LOADER_JAR);
            // 拷贝 bootx loader 相关的文件到 jar 根目录
            writer.writeLoaderClasses(NESTED_LOADER_JAR_BOOTX);
        }

        @Override
        public String getLauncherClassName() {
            // 替换为我们自己的 JarLauncher
            return "com.meiyibao.bootx.loader.JarLauncher";
        }


        @Override
        public String getLibraryLocation(String libraryName, LibraryScope scope) {
            return "BOOT-INF/lib/";
        }

        @Override
        public String getLibraryDestination(String libraryName, LibraryScope scope) {
            return "BOOT-INF/lib/";
        }

        @Override
        public String getClassesLocation() {
            return "";
        }

        @Override
        public String getRepackagedClassesLocation() {
            return "BOOT-INF/classes/";
        }

        @Override
        public String getClasspathIndexFileLocation() {
            return "BOOT-INF/classpath.idx";
        }

        @Override
        public String getLayersIndexFileLocation() {
            return "BOOT-INF/layers.idx";
        }

        @Override
        public boolean isExecutable() {
            return true;
        }
    }
}
