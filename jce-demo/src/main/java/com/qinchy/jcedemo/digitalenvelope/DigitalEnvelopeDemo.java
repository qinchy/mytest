package com.qinchy.jcedemo.digitalenvelope;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 演示使用Digital envelope技术进行密钥交换.
 * <p>
 * 服务器端与客户端分别由两个独立的线程模拟, 双方指间的通讯由共享内存实现.
 *
 * @author Rich, 2012-6-14.
 * @version 1.0
 * @since 1.0
 */
public final class DigitalEnvelopeDemo {

    /**
     * Client端与Server端交换数据的队列, 模拟两者之间通讯的通道, 现实中两者可能是通过Socket通讯的.
     */
    private static final BlockingQueue<Map<String, byte[]>> CHANNEL = new LinkedBlockingQueue(1);

    public static void main(String[] args) {
        KeyPair pair = generatorKeyPair();
        Thread client = new Client(pair);
        Thread server = new Server(pair.getPublic());
        server.start();
        client.start();
    }

    /**
     * 生成RSA算法的公私密钥对.
     *
     * @return 生成RSA算法的公私密钥对.
     */
    public static KeyPair generatorKeyPair() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        random.setSeed(53);
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();
        return pair;
    }

    /**
     * 根据算法和长度生成对称密钥
     *
     * @param algorithm 算法
     * @param length    长度
     * @return 对称密钥
     * @throws NoSuchAlgorithmException
     */
    private static Key getKey(String algorithm, int length) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(length);
        Key key = generator.generateKey();
        return key;
    }

    /**
     * 模拟密钥交换的服务器端, 服务器端与客户端通过共享内存来交换Digital Envelope.
     *
     * @author Rich, 2012-6-14.
     * @version 1.0
     * @since 1.0
     */
    static class Server extends Thread {

        /**
         * 客户端的公钥.
         */
        private final PublicKey clientPublicKey;

        /**
         * 实际中有可能是客户端在请求服务器端时上送了自己的公钥, 也有可能是在注册
         * 时就在服务器端登记了公钥.
         *
         * @param clientPublicKey 客户端的公钥.
         */
        public Server(PublicKey clientPublicKey) {
            this.clientPublicKey = clientPublicKey;
        }

        @Override
        public void run() {
            try {
                String msg = "Legend of AK47";
                // 生成DES的对称密钥，用来加密数据
                Key sessionKey = getKey("DES", 56);
                Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
                byte[] p = msg.getBytes(StandardCharsets.UTF_8);
                // 使用对称密钥对数据进行加密
                byte[] msgCipher = cipher.doFinal(p);

                // 使用RSA公钥对上面的DES密钥加密
                cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
                byte[] keyCipher = cipher.doFinal(sessionKey.getEncoded());


                Map<String, byte[]> result = new HashMap();
                // 消息密文，即用DES对称密钥加密消息明文的数据
                result.put("msg", msgCipher);
                // 密钥的密文，要解密消息密文必须要这个密文，所以很形象的称之为数字信封
                result.put("key", keyCipher);

                // 将消息密文和信封放到队列中，让客户端来取
                CHANNEL.offer(result);
            } catch (KeyException | NoSuchAlgorithmException e) {
                // TODO Exception handling...
            } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * 模拟密钥交换的客户端, 服务器端与客户端通过共享内存来交换Digital Envelope.
     *
     * @author Rich, 2012-6-14.
     * @version 1.0
     * @since 1.0
     */
    static class Client extends Thread {

        /**
         * 客户端的公私密钥对.
         */
        private final KeyPair keyPair;

        /**
         * 密钥对应该在客户端内部产生, 然后客户端在请求服务器端时上送了自己的公钥, 也有可能是在注册时就在服务器端登记了公钥.
         *
         * @param keyPair 客户端的公私密钥对.
         */
        public Client(KeyPair keyPair) {
            this.keyPair = keyPair;
        }

        @Override
        public void run() {
            try {
                // 从通道中获取消息密文和信封
                Map<String, byte[]> received = CHANNEL.take();
                // 消息密文，要解开它需要先使用RSA私钥解开数字信封，得到对称密钥后再解开密文
                byte[] msgCipher = received.get("msg");
                // 数字信封
                byte[] keyCipher = received.get("key");

                // 使用客户端的私钥解密数字信封
                PrivateKey privateKey = keyPair.getPrivate();
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] encoded = cipher.doFinal(keyCipher);

                // 得到解开的对称密钥，再使用对称加密算法解密消息密文
                KeySpec keySpec = new DESKeySpec(encoded);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
                // 根据对称密钥生成key
                Key key = factory.generateSecret(keySpec);
                cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] msg = cipher.doFinal(msgCipher);

                // 得到消息明文
                String plainText = new String(msg, StandardCharsets.UTF_8);
                System.out.println(plainText);
            } catch (Exception e) {
                // TODO Exception handling...
            }
        }
    }
}