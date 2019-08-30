package io.wordlift.geonames;


import com.jsoniter.JsonIterator;
import io.netty.handler.codec.http.QueryStringEncoder;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class FindNearBy implements Op<FeatureBase[]> {

    private final static String PATH = "/findNearbyJSON";

    @NonNull
    private Double latitude;

    @NonNull
    private Double longitude;

    private String featureClass;

    private String[] featureCodes;

    private String[] noFeatureCodes;

    private Double radius;

    private Integer rows;

    private Style style;

    public String getPath() {

        val qs = new QueryStringEncoder(PATH);

        assert null != latitude;
        assert null != longitude;

        qs.addParam("lat", latitude.toString());
        qs.addParam("lng", longitude.toString());

        if (null != featureClass)
            qs.addParam("featureClass", featureClass);

        if (null != featureCodes)
            for (val featureCode : featureCodes)
                qs.addParam("featureCode", featureCode);

        if (null != noFeatureCodes)
            for (val noFeatureCode : noFeatureCodes)
                qs.addParam("featureCode!", noFeatureCode);

        if (null != radius)
            qs.addParam("radius", radius.toString());

        if (null != rows)
            qs.addParam("rows", rows.toString());

        if (null != style)
            qs.addParam("style", style.toString());

        return qs.toString();
    }

    public FeatureBase[] execute(byte[] bytes) {

        return JsonIterator.deserialize(bytes)
                .get("geonames")
                .as(FeatureBase[].class);
    }

    enum Style {
        SHORT,
        MEDIUM,
        LONG,
        FULL;
    }

}
