package org.gcit.tests;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.gcit.constants.FrameworkConstants;
import org.gcit.enums.ConfigProperties;
import org.gcit.requestbuilder.ApiResponse;
import org.gcit.requestbuilder.RequestBuilder;
import org.gcit.utils.FileReadUtils;
import org.gcit.utils.PropertyUtils;
import org.testng.annotations.Test;

public class Test_Emp extends RequestBuilder {
  @Test
  public void TC_001_CreateEmp_POST_Request(Map<String, String> data) {
    ApiResponse apiResponse = requestCall(PropertyUtils.getValue(ConfigProperties.AUTHTYPE), "POST",
        data.get("endpoints"), data.get("endpointname"), null,
        "plain", FileReadUtils.getJsonFileLocationAsString(FrameworkConstants.getJsonPayload("createEmp.json")), false);
    Assertions.assertThat(apiResponse.isBodyVerificationFlag()).isEqualTo(true);
  }
  @Test
  public void TC_002_ReadAllEmp_GET_Request(Map<String, String> data) {
    ApiResponse apiResponse = requestCall(PropertyUtils.getValue(ConfigProperties.AUTHTYPE), "GET",
        data.get("endpoints"), data.get("endpointname"), null,
        "plain", null, false);
    Assertions.assertThat(apiResponse.isBodyVerificationFlag()).isEqualTo(true);
  }
  @Test
  public void TC_003_ReadAnEmp_GET_Request(Map<String, String> data) {
    Map<String, Object> resultMap = new HashMap<>(); resultMap.put("status", "success"); resultMap.put("message", "Successfully! Record has been fetched.");
    ApiResponse apiResponse = requestCall(PropertyUtils.getValue(ConfigProperties.AUTHTYPE), "GET",
        data.get("endpoints"), data.get("endpointname"), resultMap,
        "json", null, false);
    Assertions.assertThat(apiResponse.isBodyVerificationFlag()).isEqualTo(true);
  }
  @Test
  public void TC_004_UpdateEmp_PUT_Request(Map<String, String> data) {
    ApiResponse apiResponse = requestCall(PropertyUtils.getValue(ConfigProperties.AUTHTYPE), "PUT",
        data.get("endpoints"), data.get("endpointname"), null,
        "plain", FileReadUtils.getJsonFileLocationAsString(FrameworkConstants.getJsonPayload("updateEmp.json")), false);
    Assertions.assertThat(apiResponse.isBodyVerificationFlag()).isEqualTo(true);
  }
  @Test
  public void TC_005_DeleteEmp_DELETE_Request(Map<String, String> data) {
    ApiResponse apiResponse = requestCall(PropertyUtils.getValue(ConfigProperties.AUTHTYPE), "DELETE",
        data.get("endpoints"), data.get("endpointname"), null,
        "plain", null, false);
    Assertions.assertThat(apiResponse.isBodyVerificationFlag()).isEqualTo(true);
  }

}
