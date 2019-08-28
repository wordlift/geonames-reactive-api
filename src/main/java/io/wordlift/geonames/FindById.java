package io.wordlift.geonames;


import com.jsoniter.JsonIterator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Builder
public class FindById implements Op<FindById.Feature> {

    private final String PATH = "/getJSON";

    @NonNull
    private Long id;

    @Override
    public String getPath() {
        return new PathOps(this.PATH).addParam("geonameId", this.id.toString()).get();
    }

    @Override
    public Feature execute(byte[] bytes) {
        return JsonIterator.deserialize(bytes)
                .as(Feature.class);
    }

    @Getter
    public static class Feature {

        private Long geonameId;

        private String lat;
        private String lng;
        private String fcode;
        private String name;
        private String wikipediaURL;

        private String countryCode;
        private String countryId;
        private String adminId1;
        private String adminId2;
        private String adminId3;
        private String adminId4;
        private String adminId5;

        private BoundingBox bbox;

        private AlternateName[] alternateNames;

        @Getter
        public static class BoundingBox {

            private Double east;
            private Double south;
            private Double north;
            private Double west;
            private Integer accuracyLevel;

        }

        @Getter
        public static class AlternateName {

            private Boolean isPreferredName;
            private Boolean isShortName;
            private String name;
            private String lang;

        }
    }


}