package com.github.doobo.utils;

import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;

import java.security.PublicKey;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * SM基础工具
 */
public class SmUtils {

	private static final Map<String, SM2> SM2S = new WeakHashMap<>();

	/**
	 * SM2编码
	 */
	public static String encryptBySM2(String publicKey, String rec){
		if(publicKey == null || publicKey.isEmpty()){
			return null;
		}
		return getSm2ByPublicKeyCache(publicKey).encryptHex(rec, KeyType.PublicKey);
	}

	/**
	 * SM2解码
	 */
	public static String decryptBySM2(String privateKey, String rec){
		if(privateKey == null || privateKey.isEmpty()){
			return null;
		}
		return getSm2ByPrivateKeyCache(privateKey).decryptStr(rec, KeyType.PrivateKey);
	}

	/**
	 * 缓存SM2公钥
	 */
	public static SM2 getSm2ByPublicKeyCache(String publicKey){
		SM2 sm2;
		if(SM2S.containsKey(publicKey)){
			sm2 = SM2S.get(publicKey);
		}else{
			PublicKey v1 = BCUtil.decodeECPoint(publicKey, "sm2p256v1");
			sm2 = SmUtil.sm2();
			sm2.setPublicKey(v1);
			sm2.usePlainEncoding();
			SM2S.put(publicKey, sm2);
		}
		return sm2;
	}

	/**
	 * 缓存SM2私钥
	 */
	public static SM2 getSm2ByPrivateKeyCache(String privateKey){
		SM2 sm2;
		if(SM2S.containsKey(privateKey)){
			sm2 = SM2S.get(privateKey);
		}else{
			ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(privateKey);
			sm2 = new SM2(privateKeyParameters, null);
			sm2.usePlainEncoding();
			SM2S.put(privateKey, sm2);
		}
		return sm2;
	}
}
