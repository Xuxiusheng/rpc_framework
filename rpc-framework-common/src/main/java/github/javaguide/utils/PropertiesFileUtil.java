package github.javaguide.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
public final class PropertiesFileUtil {
    /**
     * 工具类，构造方法私有化，避免外部实例化
     */
    private PropertiesFileUtil() {

    }

    public static Properties readPropertiesFile(String filename) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String rpcConfigPath = "";
        if(url != null) {
            rpcConfigPath = url.getPath() + filename;
        }
        Properties properties = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(rpcConfigPath), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            log.error("occur exception when read properties file [{}]", filename);
        }
        return properties;
    }
}
