package com.aruns.sirvacs_a_lot.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class District {
    @JsonProperty("state_id")
    private Integer stateId;
    @JsonProperty("district_id")
    private Integer districtId;
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("district_name_l")
    private String districtNameL;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("state_id")
    public Integer getStateId() {
        return stateId;
    }

    @JsonProperty("state_id")
    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    @JsonProperty("district_id")
    public Integer getDistrictId() {
        return districtId;
    }

    @JsonProperty("district_id")
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @JsonProperty("district_name")
    public String getDistrictName() {
        return districtName;
    }

    @JsonProperty("district_name")
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @JsonProperty("district_name_l")
    public String getDistrictNameL() {
        return districtNameL;
    }

    @JsonProperty("district_name_l")
    public void setDistrictNameL(String districtNameL) {
        this.districtNameL = districtNameL;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
