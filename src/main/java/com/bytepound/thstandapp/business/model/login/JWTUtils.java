package com.bytepound.thstandapp.business.model.login;

import com.bytepound.thstandapp.api.rsp.UserLoginVO;
import com.bytepound.thstandapp.business.model.constant.ExtInfoConstant;
import com.google.common.reflect.TypeToken;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class JWTUtils {
    public final static String SECRET = "secretefsadfaserfawefdsafawefasfdasfaesftgerfasefdsagasdfteafdasfasdgeraregfgsrgsrsgsfhrsgfsgrhsdfgsrhrsfhgsrhsrhsrhsfgsrhgsfdghsdfhrtsfgsrtsghfsdhrsrsrsgdrfgsrhrsdfsadfsdfsdccdcvdfgrfeafasdfdsvasdfasefasdvadffasef774894jf333klkjkn2464umfkdsi95jtgo05l0-lsdfmsd";


    /**
     * 生成token
     * @param payloadStr
     * @param secret
     * @return
     */
    public static String generateTokenByHMAC (String payloadStr, String secret) {
        try {
            //准备JWS-header
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT).build();
            //将负载信息装载到payload
            Payload payload = new Payload(payloadStr);
            //封装header和payload到JWS对象
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            //创建HMAC签名器
            JWSSigner jwsSigner = new MACSigner(secret);
            //签名
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (KeyLengthException e) {
            //TODO
            e.printStackTrace();
        } catch (JOSEException e) {
            //TODO
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据token 反向获取用户信息
     * @param token
     * @param secret
     * @return
     */
    public static String verifyTokenByHMAC(String token, String secret) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            //创建HMAC验证器
            JWSVerifier jwsVerifier = new MACVerifier(secret);
            if (!jwsObject.verify(jwsVerifier)) {
                log.error("token verify failed, please use valid token!");
                throw new RuntimeException("token verify failed!");
            }
            String payload = jwsObject.getPayload().toString();
            return payload;
        } catch (ParseException | JOSEException e) {
            log.error("token parsing error! please check the token, reason:{}", e.toString());
            throw new RuntimeException(e);
        }
    }

    public static UserLoginVO getUserLoginVOPayload(String token, String secret) {
        String objStr;
        try {
            objStr = verifyTokenByHMAC(token, secret);
        } catch (RuntimeException e) {
            throw e;
        }
        JWTPayload<UserLoginVO> jwtPayload = ExtInfoConstant.gson.fromJson(objStr, new TypeToken<JWTPayload<UserLoginVO>>() {
        }.getType());

        return jwtPayload.getBody();
    }
}
