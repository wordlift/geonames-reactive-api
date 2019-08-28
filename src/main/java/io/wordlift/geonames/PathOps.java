package io.wordlift.geonames;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
class PathOps {

    private final String path;

    @SneakyThrows
    public PathOps addParam(String name, String value) {

        val join = this.path.contains("?") ? "&" : "?";

        return new PathOps(this.path + join
                + URLEncoder.encode(name, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20")
                + "="
                + URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20"));
    }

    public String get() {

        return this.path;
    }

}
