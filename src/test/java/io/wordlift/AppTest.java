package io.wordlift;

import io.wordlift.geonames.FindById;
import io.wordlift.geonames.FindNearBy;
import io.wordlift.geonames.GeonamesApi;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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

        StepVerifier.create(client.execute(
                FindNearBy.builder()
                        .latitude(41.905739)
                        .longitude(12.481812)
                        .featureClass("A")
                        .build())
                .flatMapMany(Flux::fromArray)
                .flatMap(f -> client.execute(
                        FindById.builder()
                                .id(f.getGeonameId())
                                .build()))
                .flatMap(f -> Flux.just(f.getCountryId(), f.getAdminId1(), f.getAdminId2(), f.getAdminId3(), f.getAdminId4(), f.getAdminId5())
                        .filter(s -> IS_NUMBER.matcher(s).matches())
                        .map(Long::parseLong)
                        .parallel(4)
                        .flatMap(id -> client.execute(FindById.builder()
                                .id(id)
                                .build()))
                        .sequential()
                        .sort((f1, f2) -> {

                            if (f1.getFcode().equals(f2.getFcode()))
                                return 0;

                            if (!f2.getFcode().startsWith("ADM"))
                                return -1;

                            return f2.getFcode().compareTo(f1.getFcode());
                        })))
                .assertNext(f -> Assert.assertEquals(11396114, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(11396080, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3169071, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3169069, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3174976, (long) f.getGeonameId()))
                .assertNext(f -> Assert.assertEquals(3175395, (long) f.getGeonameId()))
                .verifyComplete();

        assertTrue(true);
    }

}
