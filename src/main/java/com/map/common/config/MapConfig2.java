package com.map.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 通过构造函数绑定
 * 1. 启动项中添加 @ConfigurationPropertiesScan
 * 2. @EnableConfigurationProperties、@ConfigurationProperties、@ConstructorBinding 三者结合
 * <p>
 * 此时不能使用@Configuration or @Component
 *
 * @see com.map.CommonMapApplication
 */
@Data
@EnableConfigurationProperties({MapConfig2.class})
@ConfigurationProperties(prefix = "map2")
@ConstructorBinding
public class MapConfig2 {
    private BaiduMapConfig baiduMapConfig;
    private GaodeMapConfig gaodeMapConfig;
    private String coordinateType;

    public MapConfig2(BaiduMapConfig baiduMapConfig, GaodeMapConfig gaodeMapConfig, String coordinateType) {
        this.baiduMapConfig = baiduMapConfig;
        this.gaodeMapConfig = gaodeMapConfig;
        this.coordinateType = coordinateType;
    }

    @Data
    public static class BaiduMapConfig {
        private String urlPoi;
        private String urlTransfer;
        private String urlGeocoding;
        private String urlReverseGeocoding;
        private String key;
    }

    @Data
    public static class GaodeMapConfig {
        private String urlPoi;
        private String urlTransfer;
        private String urlGeocoding;
        private String urlReverseGeocoding;
        private String key;
    }
}
