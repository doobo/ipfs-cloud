package ipfs.common;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.junit.jupiter.api.Test;

/**
 * sm2简单测试
 */
public class Sm2ComTests {

	/**
	 * 生成密钥对
	 */
	@Test
	public void createKeys(){
		SM2 sm2 = SmUtil.sm2();
		String privateKey = HexUtil.encodeHexStr(sm2.getPrivateKey().getEncoded());
		String publicKey = HexUtil.encodeHexStr(sm2.getPublicKey().getEncoded());
		System.out.println("private:" + privateKey);
		System.out.println("public:" + publicKey);
		System.out.println();

		//JS端兼容,q值作为JS端加密公钥,d值作为私钥
		//((BCECPrivateKey) privateKey).getD().toByteArray();
		privateKey = HexUtil.encodeHexStr(((BCECPrivateKey) sm2.getPrivateKey()).getD().toByteArray());
		String tmp =  HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm2.getPrivateKey()));
		System.out.println("private:" + privateKey);
		System.out.println("private-tmp:" + tmp);

		//((BCECPublicKey) publicKey).getQ().getEncoded(true)
		publicKey = HexUtil.encodeHexStr(((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false));
		tmp = HexUtil.encodeHexStr(BCUtil.encodeECPublicKey(sm2.getPublicKey()));
		System.out.println("publicKey:" + publicKey);
		System.out.println("publicKey-tmp:" + tmp);
	}
}
