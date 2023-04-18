package com.qinchy.dynamiceval.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;

import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import static cn.hutool.crypto.Mode.CBC;
import static cn.hutool.crypto.Padding.ZeroPadding;

public class SM4Util {

    private static final byte[] KEY = "abc1111111111333".getBytes(CharsetUtil.CHARSET_UTF_8);
    private static final byte[] IV = "iviviviviviviviv".getBytes(CharsetUtil.CHARSET_UTF_8);

    public static String encrypt(String plainText) {
        String cipherTxt = "";
        SymmetricCrypto sm4 = new SM4(CBC, ZeroPadding, KEY, IV);
        byte[] encrypHex = sm4.encrypt(plainText);
        cipherTxt = Base64.encode(encrypHex);
        return "{SM4}" + cipherTxt;
    }

    public static String decrypt(String cipherText) {
        if (!cipherText.startsWith("{SM4}")) {
            return cipherText;
        }
        cipherText = cipherText.substring(5);
        String plainText = "";
        SymmetricCrypto sm4 = new SM4(CBC, ZeroPadding, KEY, IV);
        byte[] cipherHex = Base64.decode(cipherText);
        plainText = sm4.decryptStr(cipherHex, CharsetUtil.CHARSET_UTF_8);
        return plainText;
    }

    public static void main(String[] args) {
        String originTxt = "测试";
        System.out.println("原文: " + originTxt);
        String cipherTxt = encrypt(originTxt);
        System.out.println("密文: " + cipherTxt);
        String plainTxt = decrypt(cipherTxt);
        System.out.println("解密结果: " + plainTxt);
    }
}