package com.bytepound.thstandapp.common.model.login;

import lombok.Data;

@Data
public class IpLocationInfo {
    private String country;
    private String province;
    private String city;
    private String isp;
}
