package com.qinchy.dynamiceval.utils;


import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;

/**
 * SM2算法工具类
 */
public class Sm2CryptTools {
    private final KeyPair mKeyPair;

    public Sm2CryptTools() throws Exception {
        mKeyPair = initKey();
    }

    /**
     * 测试函数
     */
    public static void Sm2Test() {
        String dataStr = "hello ,2023!";
        try {
            Sm2CryptTools sm2CryptTools = new Sm2CryptTools();
            KeyPair keyPair = sm2CryptTools.mKeyPair;

            System.out.println("原始明文：" + dataStr);
            String resData = sm2CryptTools.encrypt(keyPair.getPublic(), dataStr);
            System.out.println("SM2加密后密文：" + resData);

            String resData2 = sm2CryptTools.decrypt(keyPair.getPrivate(), resData);
            System.out.println("SM2解密后明文：" + resData2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) {
        Sm2Test();
    }

    /**
     * 创建密钥对
     *
     * @return 密钥对KeyPair
     * @throws Exception
     */
    public KeyPair initKey() throws Exception {
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        // 使用SM2参数初始化生成器
        kpg.initialize(sm2Spec);
        // 获取密钥对
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * sm2加密算法
     *
     * @param pubkey：公钥
     * @param plainData：要加密的字符串
     * @return：加密结果
     */
    public String encrypt(PublicKey pubkey, String plainData) {
        try {
            SM2Engine sm2Engine = MySm2Engine.createMySm2Engine(pubkey, null, MySm2Engine.Type_Encode);
            //encrypt data
            byte[] bytes = null;
            try {
                byte[] in = plainData.getBytes(StandardCharsets.UTF_8);
                bytes = sm2Engine.processBlock(in, 0, in.length);
            } catch (Exception e) {
                System.out.println("SM2加密失败:");
            }
            return Hex.toHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * sm2解密算法
     *
     * @param priKey：私钥
     * @param cipherData：要解密的字符串
     * @return
     */
    public String decrypt(PrivateKey priKey, String cipherData) {
        try {
            //init engine
            SM2Engine sm2Engine = MySm2Engine.createMySm2Engine(null, priKey, MySm2Engine.Type_Decode);

            //decrypt data
            byte[] cipherDataByte = Hex.decode(cipherData);
            byte[] bytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("SM2解密失败:");
        }

        return null;
    }

}

