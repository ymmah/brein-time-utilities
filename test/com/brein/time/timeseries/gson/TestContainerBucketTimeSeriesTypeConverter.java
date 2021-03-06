package com.brein.time.timeseries.gson;

import com.brein.time.timeseries.BucketTimeSeries;
import com.brein.time.timeseries.BucketTimeSeriesConfig;
import com.brein.time.timeseries.ContainerBucketTimeSeries;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestContainerBucketTimeSeriesTypeConverter {

    static final Gson gson;

    static {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BucketTimeSeries.class, new BucketTimeSeriesTypeConverter());
        gsonBuilder.registerTypeAdapter(ContainerBucketTimeSeries.class, new ContainerBucketTimeSeriesTypeConverter());
        gson = gsonBuilder.create();
    }

    @Test
    public void testContainerBucketTimeSeriesInteger() {
        BucketTimeSeries res;

        final Random rnd = new Random();
        final ContainerBucketTimeSeries<HashSet<Integer>, Integer> ts =
                new ContainerBucketTimeSeries<>(HashSet::new, new BucketTimeSeriesConfig<>(HashSet.class, TimeUnit
                        .SECONDS, 10, 1));

        res = gson.fromJson(gson.toJson(ts), ContainerBucketTimeSeries.class);
        Assert.assertArrayEquals(ts.order(), res.order());

        for (int i = 0; i < 2; i++) {
            ts.add(System.currentTimeMillis() / 1000L - rnd.nextInt(10), rnd.nextInt(1000));
        }

        res = gson.fromJson(gson.toJson(ts), ContainerBucketTimeSeries.class);
        Assert.assertArrayEquals(ts.order(), res.order());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testContainerBucketTimeSeriesUUID() {
        ContainerBucketTimeSeries<HashSet<UUID>, UUID> res;

        final Random rnd = new Random();
        final ContainerBucketTimeSeries<HashSet<UUID>, UUID> ts =
                new ContainerBucketTimeSeries<>(HashSet::new, new BucketTimeSeriesConfig<>(HashSet.class, TimeUnit
                        .MINUTES, 2, 15));

        res = gson.fromJson(gson.toJson(ts), ContainerBucketTimeSeries.class);
        Assert.assertArrayEquals(ts.order(), res.order());

        final int rounds = Math.max(500, rnd.nextInt(3000));
        for (int i = 0; i < rounds; i++) {
            ts.add(System.currentTimeMillis() / 100000L - (rnd.nextInt(20) * 60), UUID.randomUUID());

            res = gson.fromJson(gson.toJson(ts), ContainerBucketTimeSeries.class);
            Assert.assertArrayEquals(ts.order(), res.order());
        }
    }
}
