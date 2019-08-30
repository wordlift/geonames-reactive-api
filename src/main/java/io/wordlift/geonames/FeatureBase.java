package io.wordlift.geonames;

import lombok.Getter;

@Getter
public class FeatureBase {

    private String lat;
    private String lng;

    private Long geonameId;

    private String name;
    private String toponymName;

    private String fcl;
    private String fclName;

    private String fcode;
    private String fcodeName;

    private String adminName1;

    private Long population;

}
