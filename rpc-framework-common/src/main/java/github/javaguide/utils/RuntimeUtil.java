package github.javaguide.utils;

public class RuntimeUtil {

    /**
     * 获取CPU核心数
     * @return
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
