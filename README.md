# bootx-loader
spring-boot-maven-plugin插件类加载优化

参考：
https://blog.csdn.net/javaYY_/article/details/125690216

#使用
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
            <configuration>
                <layoutFactory implementation="com.seewo.psd.bootx.loader.tools.MyLayoutFactory"/>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.seewo.psd.bootx</groupId>
            <artifactId>bootx-loader-tools</artifactId>
            <version>0.1.1</version>
        </dependency>
    </dependencies>
</plugin>
