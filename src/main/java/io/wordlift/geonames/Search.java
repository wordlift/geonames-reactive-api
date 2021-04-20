package io.wordlift.geonames;


import com.jsoniter.JsonIterator;
import io.netty.handler.codec.http.QueryStringEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Search implements Op<FeatureBase[]> {

    private final static String PATH = "/searchJSON";

    private String nameEquals;

    private String country;

    private String[] featureClasses;

    private String[] featureCodes;

    private Integer maxRows;

    public String getPath() {

        val qs = new QueryStringEncoder(PATH);

        qs.addParam("name_equals", nameEquals);

        if (null != country)
            qs.addParam("country", country);

        if (null != featureClasses)
            for (val featureClass : featureClasses)
                qs.addParam("featureClass", featureClass);

        if (null != featureCodes)
            for (val featureCode : featureCodes)
                qs.addParam("featureCode", featureCode);

        if (null != maxRows)
            qs.addParam("maxRows", String.valueOf(maxRows));

        return qs.toString();
    }

    public FeatureBase[] execute(byte[] bytes) {

        return JsonIterator.deserialize(bytes)
                .get("geonames")
                .as(FeatureBase[].class);
    }

}
