package com.map.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("map")
public class MapConfig {
    private BaiduMapConfig baiduMapConfig;
    private GaodeMapConfig gaodeMapConfig;
    private String coordinateType;

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
