package io.wordlift.geonames;

import lombok.RequiredArgsConstructor;
import lombok.val;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@RequiredArgsConstructor
public
class GeonamesApi {

    private final String baseUrl;

    private final String username;

    public <T> Mono<T> execute(Op<T> op) {

        val path = op.getPath();
        val pathWithUsername = new PathOps(path).addParam("username", this.username).get();

        return create(pathWithUsername)
                .map(op::execute);
    }

    private Mono<byte[]> create(String path) {

        return HttpClient.create()
                .baseUrl(this.baseUrl)
                .get()
                .uri(path)
                .responseSingle((resp, byteBufMono) -> byteBufMono.asByteArray());
    }

}

