package org.gcit.enums;

public enum ConfigProperties {

    /**
     * Enums to restrict the values used on Property files. Without using enums there can be created null pointer exceptions
     * because of typos.
     * Whenever a new value is added to property file, corresponding enum should be created here.
     */
    BASEURL,
    OVERRIDEREPORTS,
    EXCELREPORT,
    RETRY,
    ENV,
    AUTHTYPE,
    ACCESSTOKENURL,
    CLIENTID,
    CLIENTSECRET,
    SCOPE,
    AUDIENCE,
    AUTHPREFIX,
    USERNAME,
    PASSWORD,

    APIKEY;


}
