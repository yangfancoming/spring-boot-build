#  执行流程总结 
    1.SpringApplication.run(MyTestMVCApplication.class, args);
    2.return run(new Class<?>[] { primarySource }, args);
    3.return new SpringApplication(primarySources).run(args);
    4.public SpringApplication(Class<?>... primarySources)
    5.setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
    6.private <T> Collection<T> getSpringFactoriesInstances(Class<T> type)
    7.Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
    8.Enumeration<URL> urls = classLoader != null ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories");
    
    　终于到最后了，最终我们就是要看上图有一行 Enumeration<URL> urls = classLoader != null ? classLoader.getResources("META-INF/spring.factories")
    　看看类路径（遍历所有jar包下）"META-INF/spring.factories"这个路径下放了一些什么鬼组件就ok了，在左侧打开所有的jar包（此处，factoryClassName传进来的是EnableAutoConfiguration.class,根据这个类名才能对应目标factories文件下的键，然后取出键对应的值）
      上图里面这么多的xxxAutoConfiguration就是我们的这么久得出的结果，最终就是加载这么多的类的全路径，然后springboot内部就是实例化这些类并加载到容器里面，完成springboot应用启动时的自动配置


    总结一下整个过程：
    
    springboot应用启动------>
    @SpringBootApplication起作用--------->
    @EnableAutoConfiguration--------->
    【@AutoConfigurationPackage：扫描主配置类同级目录以及子包】
    【@Import({EnableAutoConfigurationImportSelector.class})：导入一个自动配置组件】---------->
    EnableAutoConfigurationImportSelector extends AutoConfigurationImportSelector ------------>
    public String[] selectImports() {List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);}---------->
    List<String> configurations = SpringFactoriesLoader.loadFactoryNames()---------->
    classLoader.getResources("META-INF/spring.factories")--------------->
    spring-boot-autoconfigure\1.5.9.RELEASE\spring-boot-autoconfigure-1.5.9.RELEASE.jar!\META-INF\spring.factories--------->
    factories里面的各种xxxxAutoConfiguration的全类名
    自己多走几遍就会了，熟能生巧，等把springboot原理学会了，再用起来才能更得心应手
