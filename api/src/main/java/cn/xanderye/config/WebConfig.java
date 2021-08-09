package cn.xanderye.config;

import cn.xanderye.filter.CorsFilter;
import cn.xanderye.interceptor.LoginInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(this.loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/captcha")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/check");
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        /**
         * 1.先定义一个convert转换消息的对象
         * 2.添加fastjson的配置信息，比如：是否要格式化返回的json数据
         * 3.在convert中添加配置信息
         * 4.将convert添加到converters当中
         */
        //1.先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2.添加fastjson的配置信息，比如：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat);

        //处理中文乱码问题(不然出现中文乱码)
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        //3.在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters(fastConverter);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CorsFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}
