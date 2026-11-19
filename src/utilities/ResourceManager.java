package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ResourceManager {

    private ResourceManager() {
    }

    public static InputStream open(String path) throws IOException {
        InputStream resource = ResourceManager.class.getResourceAsStream(path);
        if (resource != null) {
            return resource;
        }

        String filePath = path.startsWith("/") ? path.substring(1) : path;
        return new FileInputStream(filePath);
    }
}
