package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    //核心是Producer接口，有两个方法：创建验证码createImage、文字创建图片createText，有一个默认实现类DefaultKaptcha.class
    //服务启动时被自动装配到容器中，通过容器可以得到一个Producer实例，可以创建验证码和图片
    @Bean
    public Producer kaptchaProducer() {
        //可以从配置文件中读，properties的key很长，就只在这里写了
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");//采用哪个噪声类？

        DefaultKaptcha kaptcha = new DefaultKaptcha();//Producer接口的默认实现类，实例化
        Config config = new Config(properties);//传入参数properties(相当于一个map)，封装到config对象。
        kaptcha.setConfig(config);
        return kaptcha;
    }
}

