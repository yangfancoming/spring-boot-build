#  @SpringBootApplication
    @SpringBootApplication
        @EnableAutoConfiguration 
            @AutoConfigurationPackage
                @Import(AutoConfigurationPackages.Registrar.class)
                    Registrar 将主配置类所在包及子包下的所有组件扫描到Spring容器中！
                    
            @Import(AutoConfigurationImportSelector.class) // 关键        
            AutoConfigurationImportSelector类下的selectImports方法，会给容器中导入非常多的自动配置类(xxxxAutoConfiguration)
                    
# 
    本项目 
        <parent>
            <groupId>com.goat.springboot.learing</groupId>
            <artifactId>springboot</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </parent>
            
    点击 <artifactId>springboot</artifactId> 进入父项目
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.0.4.RELEASE</version>
            <relativePath/>
        </parent>
        
    再点击  <groupId>org.springframework.boot</groupId> 进入 springboot 
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.0.4.RELEASE</version>
            <relativePath>../../spring-boot-dependencies</relativePath>
        </parent>
        
     再点击 <groupId>org.springframework.boot</groupId>  进入依赖管理 
     可以看到 springboot 已经为我们设置好了 常用jar包依赖的默认版本号
     这就是为什么 我们添加常用jar依赖后  不用指定 
        <version>1.2.7</version>
     的原因                   
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    