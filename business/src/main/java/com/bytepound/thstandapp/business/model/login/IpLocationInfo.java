package com.bytepound.thstandapp.business.model.login;

import lombok.Data;

@Data
public class IpLocationInfo {
    private String country;
    private String province;
    private String city;
    private String isp;
}
