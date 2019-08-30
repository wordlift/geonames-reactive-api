package io.wordlift.geonames;

import com.jsoniter.JsonIterator;
import io.netty.handler.codec.http.QueryStringEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.val;

@AllArgsConstructor
@Builder
public class Hierarchy implements Op<FeatureBase[]> {

    private final static String PATH = "/hierarchyJSON";

    private final Long id;

    @Override
    public String getPath() {

        val qs = new QueryStringEncoder(PATH);

        qs.addParam("geonameId", this.id.toString());

        return qs.toString();

    }

    @Override
    public FeatureBase[] execute(byte[] bytes) {

        return JsonIterator.deserialize(bytes)
                .get("geonames")
                .as(FeatureBase[].class);
    }

}
