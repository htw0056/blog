# maven打包jar文件

### 方法一

**注意**，使用该方法无法在运行jar包时执行指定的类。

1. 在项目的POM.xml文件中添加maven-assembly-plugin插件，如下：

   ```xml
   <build>
       <plugins>
           <plugin>
               <artifactId>maven-assembly-plugin</artifactId>
               <configuration>
                   <archive>
                       <manifest>
                           <mainClass>com.htw.test.TestMaven</mainClass>
                       </manifest>
                   </archive>
                   <descriptorRefs>
                       <descriptorRef>jar-with-dependencies</descriptorRef>
                   </descriptorRefs>
               </configuration>
           </plugin>
       </plugins>
   </build>
   ```

   **其中mainClass是你项目的入口main函数所在的类**。根据你实际的情况修改即可。

2. 执行`mvn assembly:assembly`，在项目下的`target`文件夹中生成两个jar文件:`xxx.jar`与`xxx-jar-with-dependencies.jar, xxx`。`xxx.jar`便是不包含依赖的jar，`xxx-jar-with-dependencies.jar`便是包含依赖的jar包。

3. 如果在终端或者shell文件中使用java命令运行，便需要运行xxx-jar-with-dependencies.jar，不然会报依赖的包不存在的错误。使用`java -jar xxx-jar-with-dependencies.jar arg1, arg2 … 1> info.log 2> err.log` 运行jar包，1>info.log表示将运行时jar包中的终端输出到info.log文件中，2>err.log表示将运行时的错误信息输出到err.log文件中。


### 方法二


**注意**，使用该方法可以在运行jar包时执行指定的类。

1. 修改POM.XML文件

   ```Xml
   <build>
   	<plugins>
   		<plugin>
   			<artifactId>maven-assembly-plugin</artifactId>
   			<configuration>
   				<descriptorRefs>
   					<descriptorRef>jar-with-dependencies</descriptorRef>
   				</descriptorRefs>
   			</configuration>
   			<executions>
   				<execution>
   					<id>make-assembly</id>
   					<phase>package</phase>
   					<goals>
   						<goal>single</goal>
   					</goals>
   				</execution>
   			</executions>
   		</plugin>
   	</plugins>
   </build>
   ```

2. 执行 ` mvn clean package`，在项目下的`target`文件夹中生成两个jar文件，`xxx.jar`与`xxx-jar-with-dependencies.jar`.

3. **这里不同于方法一**,运行jar包时使用的命令为`java -cp xxx-jar-with-dependencies.jar com.htw.test.TestMaven`选项使用的是-cp，而不是-jar,而且需要指定运行的class。