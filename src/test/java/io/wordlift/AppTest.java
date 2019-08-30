package io.wordlift;

import io.wordlift.geonames.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {

        // http://api.geonames.org/findNearbyJSON?lat=41.905739&lng=12.481812&username=cus_luxuryretreats&featureClass=A

        //


        val baseUrl = "http://api.geonames.org";
        val username = "cus_luxuryretreats";

        val client = new GeonamesApi(baseUrl, username);


        val IS_NUMBER = Pattern.compile("^\\d+$");

        val sequence = new AtomicLong();

        StepVerifier.create(client.execute(
                FindNearBy.builder()
                        .latitude(21.096889)
                        .longitude(-86.766289)
                        .featureClass("P")
                        .build())
                .flatMapMany(Flux::fromArray)
                .flatMap(f -> client.execute(
                        Hierarchy.builder()
                                .id(f.getGeonameId())
                                .build()))
                .flatMap(fs -> Flux.fromArray(fs)
                        .filter(f -> "A".equals(f.getFcl()) || "P".equals(f.getFcl()))
                        .map(FeatureBase::getGeonameId)
                        .zipWith(Flux.range(0, fs.length))
                        .parallel(4)
                        .flatMap(t -> client.execute(FindById.builder()
                                .id(t.getT1())
                                .build())
                                .map(f -> Tuples.of(f, t.getT2())))
                        .sequential())
                .sort(Comparator.<Tuple2<FindById.Feature, Integer>>comparingInt(Tuple2::getT2).reversed())
                .map(Tuple2::getT1))
                .assertNext(f -> Assert.assertEquals(7649125, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3531673, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(8581707, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3520887, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3996063, (long) f.getGeonameId()))
                .verifyComplete();

        assertTrue(true);
    }

}
