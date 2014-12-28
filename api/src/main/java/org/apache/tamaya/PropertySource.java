/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.tamaya;

import org.apache.tamaya.spi.ConfigChangeSetCallback;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * This interface models a provider that serves configuration properties. The contained
 * properties may be read fromMap single or several sources (composite).<br/>
 * Property config are the building blocks out current which complex
 * configuration is setup.
 * <p/>
 * <h3>Implementation Requirements</h3>
 * <p></p>Implementations current this interface must be
 * <ul>
 * <li>Thread safe.
 * </ul>
 * It is highly recommended that implementations also are
 * <ul>
 * <li>Immutable</li>
 * <li>serializable</li>
 * </ul>
 * </p>
 */
public interface PropertySource extends PropertyMapSupplier {

    /**
     * An empty and immutable PropertyProvider instance.
     */
    public static final PropertySource EMPTY_PROPERTYSOURCE = new PropertySource() {

        @Override
        public String getName() {
            return "<empty>";
        }

        @Override
        public Optional<String> get(String key) {
            return Optional.empty();
        }

        @Override
        public void update(ConfigChangeSet changeSet) {

        }

        @Override
        public void registerForUpdate(ConfigChangeSetCallback callback) {

        }

        @Override
        public void removeForUpdate(ConfigChangeSetCallback callback) {

        }

        @Override
        public Map<String, String> getProperties() {
            return Collections.emptyMap();
        }

        @Override
        public String toString(){
            return "PropertySource [name=<empty>]";
        }
    };

    /**
     * Get the name of the property source. The name should be unique for the type of source, whereas the id is used
     * to ensure unique identity, either locally or remotely.
     * @return the configuration's name, never null.
     */
    String getName();

    /**
     * Access a property.
     *
     * @param key the property's key, not null.
     * @return the property's keys.
     */
    Optional<String> get(String key);

    /**
     * Determines if this config source should be scanned for its list of properties.
     *
     * Generally, slow ConfigSources should return false here.
     *
     * @return true if this ConfigSource should be scanned for its list of properties,
     * false if it should not be scanned.
     */
    default boolean isScannable(){
        return true;
    }

    /**
     * Allows to quickly check, if a provider is empty.
     *
     * @return true, if the provier is empty.
     */
    default boolean isEmpty() {
        return getProperties().isEmpty();
    }

    /**
     * Extension point for adjusting property sources.
     *
     * @param operator A property source operator, e.g. a filter, or an adjuster
     *                 combining property sources.
     * @return the new adjusted property source, never {@code null}.
     */
    default PropertySource with(UnaryOperator<PropertySource> operator){
        return operator.apply(this);
    }

    /**
     * Query something from a property source.
     *
     * @param query the query, never {@code null}.
     * @return the result
     */
    default <T> T query(Function<PropertySource, T> query){
        return query.apply(this);
    }

    /**
     * Upon receiving a ConfigChangeSet, the PropertySource will be updated to include
     * any of the listed changes within
     *
     * @param changeSet the changes to be invoked
     */
    void update(ConfigChangeSet changeSet);

    /**
     * Whenever this PropertySource is updated, any registered callables will be invoked
     * @param callback
     */
    void registerForUpdate(ConfigChangeSetCallback callback);

    /**
     * Removes a callback to be invoked.
     * @param callback
     */
    void removeForUpdate(ConfigChangeSetCallback callback);


}
