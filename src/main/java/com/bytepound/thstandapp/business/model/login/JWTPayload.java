package com.bytepound.thstandapp.business.model.login;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class JWTPayload<T> {

    private T body;
    // 创建时间
    private long timestamp;
    // 默认过期时间
    private long expiration;
}
