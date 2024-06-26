/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amplifyframework.analytics.pinpoint;

import androidx.annotation.NonNull;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsException;
import com.amplifyframework.annotations.InternalAmplifyApi;
import com.amplifyframework.core.configuration.AmplifyOutputsData;

/**
 * Configuration options for Amplify Analytics Pinpoint plugin.
 */
final class AWSPinpointAnalyticsPluginConfiguration {

    static final long DEFAULT_AUTO_FLUSH_INTERVAL = 30000L;

    // Pinpoint plugin configuration options
    private final String appId;
    private final String region;
    private final long autoFlushEventsInterval;
    private final boolean trackAppLifecycleEvents;

    private AWSPinpointAnalyticsPluginConfiguration(Builder builder) {
        this.appId = builder.appId;
        this.region = builder.region;
        this.autoFlushEventsInterval = builder.autoFlushEventsInterval;
        this.trackAppLifecycleEvents = builder.trackAppLifecycleEvents;
    }

    /**
     * AppId getter.
     *
     * @return appId
     */
    String getAppId() {
        return appId;
    }

    /**
     * Accessor for pinpoint region.
     *
     * @return pinpoint region.
     */
    String getRegion() {
        return region;
    }

    /**
     * Accessor for auto event flush interval.
     *
     * @return auto event flush interval.
     */
    long getAutoFlushEventsInterval() {
        return autoFlushEventsInterval;
    }

    /**
     * Is auto session tracking enabled.
     * @return Is auto session tracking enabled.
     */
    boolean isTrackAppLifecycleEvents() {
        return trackAppLifecycleEvents;
    }

    /**
     * Return a builder that can be used to construct a new instance of
     * {@link AWSPinpointAnalyticsPluginConfiguration}.
     * @return An {@link AWSPinpointAnalyticsPluginConfiguration.Builder} instance
     */
    static Builder builder() {
        return new Builder();
    }

    @InternalAmplifyApi
    static AWSPinpointAnalyticsPluginConfiguration from(
        @NonNull AmplifyOutputsData outputs,
        @NonNull AWSPinpointAnalyticsPlugin.Options options
    ) throws AmplifyException {
        AmplifyOutputsData.Analytics analytics = outputs.getAnalytics();
        if (analytics == null || analytics.getAmazonPinpoint() == null) {
            throw new AnalyticsException(
                "Missing Analytics configuration",
                "Ensure that analytics is enabled and exists in your configuration file"
            );
        }

        // Note: autoFlushEventsInterval is not supported in Gen2 config.
        // Customers should use the programmatic plugin options API instead.
        return builder()
                   .withAppId(analytics.getAmazonPinpoint().getAppId())
                   .withRegion(analytics.getAmazonPinpoint().getAwsRegion())
                   .withAutoFlushEventsInterval(options.getAutoFlushEventsInterval())
                   .withTrackAppLifecycleEvents(options.getTrackLifecycleEvents())
                   .build();
    }

    /**
     * Used for fluent construction of an immutable {@link AWSPinpointAnalyticsPluginConfiguration} object.
     */
    static final class Builder {
        private String appId;
        private String region;
        private long autoFlushEventsInterval = DEFAULT_AUTO_FLUSH_INTERVAL;
        private boolean trackAppLifecycleEvents = true;

        Builder withAppId(final String appId) {
            this.appId = appId;
            return this;
        }

        Builder withRegion(final String region) {
            this.region = region;
            return this;
        }

        Builder withTrackAppLifecycleEvents(final boolean trackAppLifecycleEvents) {
            this.trackAppLifecycleEvents = trackAppLifecycleEvents;
            return this;
        }

        Builder withAutoFlushEventsInterval(final long autoFlushEventsInterval) {
            this.autoFlushEventsInterval = autoFlushEventsInterval;
            return this;
        }

        AWSPinpointAnalyticsPluginConfiguration build() {
            return new AWSPinpointAnalyticsPluginConfiguration(this);
        }
    }
}
