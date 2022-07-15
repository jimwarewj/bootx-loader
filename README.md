# bootx-loader
spring-boot-maven-plugin插件类加载优化

参考：
https://blog.csdn.net/javaYY_/article/details/125690216


使用方法：
```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
            <configuration>
                <layoutFactory implementation="com.rewrite.bootx.loader.MybLayoutFactory"/>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
              <groupId>com.meiyibao</groupId>
            <artifactId>bootx-loader-core</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
</plugin>
```
