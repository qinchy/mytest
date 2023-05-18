package com.qinchy.jmockitdemo;

import java.io.File;
import java.net.URI;

public class JNITools {

    /**
     * 加载AnOrdinaryClass类的native方法
     *
     * @throws Throwable
     */
    public static void loadNative() throws Throwable {
        URI uri = ClassLoader.class.getResource("/").toURI();
        String libraryName = "libAnOrdinaryClass.dll";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("mac")) {
            libraryName = "libAnOrdinaryClass.dylib";
        }
        String realPath = new File(uri).getAbsolutePath() + "/" + libraryName;
        System.load(realPath);
    }

}