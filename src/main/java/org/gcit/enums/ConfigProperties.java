package org.gcit.enums;

public enum ConfigProperties {

    /**
     * Enums to restrict the values used on Property files. Without using enums there can be create null pointer exceptions
     * because of typos.
     * <p>
     * Whenever a new value is added to property file, corresponding enum should be created here.
     *
     *
     * @date 2024-07-02
     * @author Pon Vignesh K
     * @version 1.0
     * @since 1.0<br>
     * @see org.gcit.utils.PropertyUtils
     */

    BASEURL,
    OVERRIDEREPORTS,
    RETRYATTEMPTS,
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
    DATASOURCE,
    RUNMODE,
    APIKEY,
    ACCEPTHEADER,
    CONTENTTYPEHEADER,
    LOGRESPONSE;


}
