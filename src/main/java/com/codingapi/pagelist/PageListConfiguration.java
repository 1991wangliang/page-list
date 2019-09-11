package com.codingapi.pagelist;

import com.codingapi.pagelist.h2.H2Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class PageListConfiguration {


    @Configuration
    @ConditionalOnProperty(name = "codingapi.pagelist.h2path")
    class PageListConfig{

        @Bean
        @ConditionalOnMissingBean
        public TableHelper tableHelper(){
            return new TableHelper();
        }

        @Bean
        @ConditionalOnMissingBean
        public H2Helper h2Helper(H2Config h2Config){
            return new H2Helper(h2Config);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties(prefix = "codingapi.pagelist")
        public H2Config h2Config(){
            return new H2Config();
        }

        @Bean
        @ConditionalOnMissingBean
        public PageHelper pageHelper(TableHelper tableHelper,H2Helper h2Helper){
            return new PageHelper(tableHelper, h2Helper);
        }
    }

}
