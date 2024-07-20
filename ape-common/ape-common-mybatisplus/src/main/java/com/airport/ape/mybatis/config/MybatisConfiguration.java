package com.airport.ape.mybatis.config;

import com.airport.ape.mybatis.handler.MyMetaObjectHandler;
import com.airport.ape.mybatis.intecerptor.SqlBeautyInterceptor;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {


    /*  //单单打开这个，其他的会被覆盖
      @Bean
      @ConditionalOnProperty(value = "sql.beauty.fill", havingValue = "true", matchIfMissing = true)
      public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
          MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
          //加载数据源
          mybatisPlus.setDataSource(dataSource);
          //全局配置
          GlobalConfig globalConfig = new GlobalConfig();
          //配置填充器
          globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
          mybatisPlus.setGlobalConfig(globalConfig);
          return mybatisPlus;
      }*/
    @Bean
    @ConditionalOnProperty(value = "sql.beauty.show", havingValue = "true", matchIfMissing = true)
    public SqlBeautyInterceptor sqlBeautyInterceptor() {
        return new SqlBeautyInterceptor();
    }

    @Bean
    @ConditionalOnProperty(value = "sql.beauty.page", havingValue = "true", matchIfMissing = true)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    @ConditionalOnProperty(value = "sql.beauty.fill", havingValue = "true", matchIfMissing = true)
    public MyMetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }
}
