package util.aes;


import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

/*****************************************************
 * AES鍔犲瘑瑙ｅ瘑宸ュ叿
 ****************************************************/

public class AesUtil {
    
    //private static final Logger logger = Logger.getLogger(AesUtil.class); //log鏃ュ織
	//private  Logger logger = LogManager.getLogger(getClass());
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";    //"绠楁硶/妯″紡/琛ョ爜鏂瑰紡"  
    
    /*****************************************************
     * AES鍔犲瘑
     * @param content 鍔犲瘑鍐呭
     * @param key 鍔犲瘑瀵嗙爜锛岀敱瀛楁瘝鎴栨暟瀛楃粍鎴�
                姝ゆ柟娉曚娇鐢ˋES-128-ECB鍔犲瘑妯″紡锛宬ey闇�瑕佷负16浣�
                鍔犲瘑瑙ｅ瘑key蹇呴』鐩稿悓锛屽锛歛bcd1234abcd1234
     * @return 鍔犲瘑瀵嗘枃
     ****************************************************/
    
    public static String enCode(String content, String key) {
    	
        if (key == null || "".equals(key)) {
            //logger.info("key涓虹┖锛�");
            return null;
        }
        if (key.length() != 16) {
            //logger.info("key闀垮害涓嶆槸16浣嶏紒");
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //鑾峰緱瀵嗙爜鐨勫瓧鑺傛暟缁�
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //鏍规嵁瀵嗙爜鐢熸垚AES瀵嗛挜
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //鏍规嵁鎸囧畾绠楁硶ALGORITHM鑷垚瀵嗙爜鍣�
            cipher.init(Cipher.ENCRYPT_MODE, skey); //鍒濆鍖栧瘑鐮佸櫒锛岀涓�涓弬鏁颁负鍔犲瘑(ENCRYPT_MODE)鎴栬�呰В瀵�(DECRYPT_MODE)鎿嶄綔锛岀浜屼釜鍙傛暟涓虹敓鎴愮殑AES瀵嗛挜
            byte [] byte_content = content.getBytes("utf-8"); //鑾峰彇鍔犲瘑鍐呭鐨勫瓧鑺傛暟缁�(璁剧疆涓簎tf-8)涓嶇劧鍐呭涓鏋滄湁涓枃鍜岃嫳鏂囨贩鍚堜腑鏂囧氨浼氳В瀵嗕负涔辩爜            
            byte [] encode_content = cipher.doFinal(byte_content); //瀵嗙爜鍣ㄥ姞瀵嗘暟鎹�
            return Base64.encodeBase64String(encode_content); //灏嗗姞瀵嗗悗鐨勬暟鎹浆鎹负瀛楃涓茶繑鍥�
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  
    }

    /*****************************************************
     * AES瑙ｅ瘑
     * @param content 鍔犲瘑瀵嗘枃
     * @param key 鍔犲瘑瀵嗙爜,鐢卞瓧姣嶆垨鏁板瓧缁勬垚
                姝ゆ柟娉曚娇鐢ˋES-128-ECB鍔犲瘑妯″紡锛宬ey闇�瑕佷负16浣�
                鍔犲瘑瑙ｅ瘑key蹇呴』鐩稿悓
     * @return 瑙ｅ瘑鏄庢枃
     ****************************************************/
    
    public static String deCode(String content, String key) {
        if (key == null || "".equals(key)) {
            //logger.info("key涓虹┖锛�");
            return null;
        }
        if (key.length() != 16) {
            //logger.info("key闀垮害涓嶆槸16浣嶏紒");
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //鑾峰緱瀵嗙爜鐨勫瓧鑺傛暟缁�
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //鏍规嵁瀵嗙爜鐢熸垚AES瀵嗛挜
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //鏍规嵁鎸囧畾绠楁硶ALGORITHM鑷垚瀵嗙爜鍣�
            cipher.init(Cipher.DECRYPT_MODE, skey); //鍒濆鍖栧瘑鐮佸櫒锛岀涓�涓弬鏁颁负鍔犲瘑(ENCRYPT_MODE)鎴栬�呰В瀵�(DECRYPT_MODE)鎿嶄綔锛岀浜屼釜鍙傛暟涓虹敓鎴愮殑AES瀵嗛挜    
            byte [] encode_content = Base64.decodeBase64(content); //鎶婂瘑鏂囧瓧绗︿覆杞洖瀵嗘枃瀛楄妭鏁扮粍
            byte [] byte_content = cipher.doFinal(encode_content); //瀵嗙爜鍣ㄨВ瀵嗘暟鎹�
            return new String(byte_content,"utf-8"); //灏嗚В瀵嗗悗鐨勬暟鎹浆鎹负瀛楃涓茶繑鍥�
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  
    }
    /**
	 * 鎴彇瀛楃涓�
	 * @param str 寰呮埅鍙栫殑瀛楃涓�
	 * @param start 鎴彇璧峰浣嶇疆 锛� 1 琛ㄧず绗竴浣� -1琛ㄧず鍊掓暟绗�1浣嶏級
	 * @param end 鎴彇缁撴潫浣嶇疆 锛堝涓奿ndex锛�
	 * @return
	 */
	public static String sub(String str,int start,int end){
	    String result = null;

	    if(str == null || str.equals(""))
	        return "";

	    int len=str.length();
	    start = start < 0 ? len+start : start-1;
	    end= end < 0 ? len+end+1 :end;

	    return str.substring(start, end);
	}
	/**
	 * ajax 鍏ュ弬鍔犲瘑
	 * @param 
	 * @return
	 */
	public static JSONObject GetParam(Map<String, Object> map){

		String param = (String) map.get("paramObj");

		String key = "abcd1234abcd1234";

		String deResult = AesUtil.deCode(param, key);
        JSONObject jsonObject=JSONObject.fromObject(deResult);
        System.out.println(jsonObject);
        return jsonObject;
	}
	
	/**
	 * ajax 鍏ュ弬鍔犲瘑DT
	 * @param 
	 * @return
	 */
	public static Map<String, String> GetParamDT(Map<String, Object> map){
		//System.out.println(map);
		String param = (String) map.get("paramObj");
		//System.out.println(param);
		String key = "abcd1234abcd1234";
		//String p = sub(param.toString(),2,param.toString().length()-1);
		//System.out.println(p);
		String deResult = AesUtil.deCode(param, key);
		Map<String, String>  jsonObject = JSONObject.fromObject(deResult);
        System.out.println(jsonObject);
        return jsonObject;
	}
	/*****************************************************
     * AES鍔犲瘑
     * @param content 鍔犲瘑鍐呭
     * @return 鍔犲瘑瀵嗘枃
     ****************************************************/
    
    public static String enCodeByKey(String content) {
    	//System.out.println("## "+content);
    	String key = "abcd1234abcd1234";
        try {
            byte[] raw = key.getBytes();  //鑾峰緱瀵嗙爜鐨勫瓧鑺傛暟缁�
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //鏍规嵁瀵嗙爜鐢熸垚AES瀵嗛挜
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //鏍规嵁鎸囧畾绠楁硶ALGORITHM鑷垚瀵嗙爜鍣�
            cipher.init(Cipher.ENCRYPT_MODE, skey); //鍒濆鍖栧瘑鐮佸櫒锛岀涓�涓弬鏁颁负鍔犲瘑(ENCRYPT_MODE)鎴栬�呰В瀵�(DECRYPT_MODE)鎿嶄綔锛岀浜屼釜鍙傛暟涓虹敓鎴愮殑AES瀵嗛挜
            byte [] byte_content = content.getBytes("utf-8"); //鑾峰彇鍔犲瘑鍐呭鐨勫瓧鑺傛暟缁�(璁剧疆涓簎tf-8)涓嶇劧鍐呭涓鏋滄湁涓枃鍜岃嫳鏂囨贩鍚堜腑鏂囧氨浼氳В瀵嗕负涔辩爜            
            byte [] encode_content = cipher.doFinal(byte_content); //瀵嗙爜鍣ㄥ姞瀵嗘暟鎹�
            return Base64.encodeBase64String(encode_content); //灏嗗姞瀵嗗悗鐨勬暟鎹浆鎹负瀛楃涓茶繑鍥�
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  
    }
	
    public static String deCodeByKey(String content) {
    	String key = "abcd1234abcd1234";
        try {
            byte[] raw = key.getBytes();  //鑾峰緱瀵嗙爜鐨勫瓧鑺傛暟缁�
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //鏍规嵁瀵嗙爜鐢熸垚AES瀵嗛挜
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //鏍规嵁鎸囧畾绠楁硶ALGORITHM鑷垚瀵嗙爜鍣�
            cipher.init(Cipher.DECRYPT_MODE, skey); //鍒濆鍖栧瘑鐮佸櫒锛岀涓�涓弬鏁颁负鍔犲瘑(ENCRYPT_MODE)鎴栬�呰В瀵�(DECRYPT_MODE)鎿嶄綔锛岀浜屼釜鍙傛暟涓虹敓鎴愮殑AES瀵嗛挜    
            byte [] encode_content = Base64.decodeBase64(content); //鎶婂瘑鏂囧瓧绗︿覆杞洖瀵嗘枃瀛楄妭鏁扮粍
            byte [] byte_content = cipher.doFinal(encode_content); //瀵嗙爜鍣ㄨВ瀵嗘暟鎹�
            return new String(byte_content,"utf-8"); //灏嗚В瀵嗗悗鐨勬暟鎹浆鎹负瀛楃涓茶繑鍥�
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  
    }
	
    /*****************************************************
     * AES鍔犲瘑瑙ｅ瘑娴嬭瘯
     * @param args
     * @return 
     ****************************************************/
    
    public static void main(String[] args) {
        String content = "{\"username\":\"123\",\"password\":\"123\"}";
        //String content = "123456";
        //logger.info("鍔犲瘑content锛�"+content);
        String key = "abcd1234abcd1234";
        //String key = "abcd1234";
        //logger.info("鍔犲瘑key锛�"+key);
        String enResult = enCode(content, key);
        System.out.println(enResult);
        //logger.info("鍔犲瘑result锛�"+enResult);
        String deResult = "{"+deCode("P5uiboh65fGXdTCWOpqfaeDKsYonN+deX/dvcLYunCTnDLKT8uzJw0iYHVxttW9U", key)+"}";
        //String deResult = "{"+deCode("e10adc3949ba59abbe56e057f20f883e", key)+"}";
        //logger.info("瑙ｅ瘑result锛�"+deResult);
        System.out.println(deResult);
        
        
        
    }
}