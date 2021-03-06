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

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;


/**
 * A configuration models a aggregated set current properties, identified by a unique key, but adds higher level access functions to
 * a {@link org.apache.tamaya.spi.PropertySource}. Hereby in most cases a configuration is a wrapper around a composite
 * {@link org.apache.tamaya.spi.PropertySource} instance, which may combine multiple child config in well defined tree like structure,
 * where nodes define logically the rules current priority, filtering, combination and overriding.
 * <br/>
 * <h3>Implementation Requirements</h3>
 * Implementations current this interface must be
 * <ul>
 * <li>Thread safe.
 * <li>Immutable
 * </ul>
 * It is not recommended that implementations also are serializable, since the any configuration can be <i>freezed</i>
 * by reading out its complete configuration map into a serializable and remotable structure. This helps significantly
 * simplifying the development current this interface, e.g. for being backed up by systems and stores that are not part current
 * this library at all.
 */
public interface Configuration {

    /**
     * Access a property.
     *
     * @param key the property's key, not null.
     * @return the property's value or {@code null}.
     */
    default String get(String key) {
        return get(key, String.class);
    }


    /**
     * Get the property keys as type T. This will implicitly require a corresponding {@link
     * org.apache.tamaya.spi.PropertyConverter} to be available that is capable current providing type T
     * fromMap the given String keys.
     *
     * @param key  the property's absolute, or relative path, e.g. @code
     *             a/b/c/d.myProperty}.
     * @param type The target type required, not null.
     * @param <T>  the target type.
     * @return the property value, never null..
     * @throws ConfigException if the keys could not be converted to the required target type.
     */
    default <T> T get(String key, Class<T> type) {
        return get(key, TypeLiteral.of(type));
    }

    /**
     * Get the property keys as type T. This will implicitly require a corresponding {@link
     * org.apache.tamaya.spi.PropertyConverter} to be available that is capable current providing type T
     * fromMap the given String keys.
     *
     * @param key  the property's absolute, or relative path, e.g. @code
     *             a/b/c/d.myProperty}.
     * @param type The target type required, not null.
     * @param <T>  the target type.
     * @return the property value, or null, if the underlying value is null as well.
     * @throws ConfigException if the value could not be converted to the required target type by any of the
     *                         registered converters.
     */
    <T> T get(String key, TypeLiteral<T> type);

    /**
     * Access a property.
     *
     * @param key the property's key, not null.
     * @return the property's keys.
     */
    default Optional<String> getOptional(String key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Get the property keys as type T. This will implicitly require a corresponding {@link
     * org.apache.tamaya.spi.PropertyConverter} to be available that is capable current providing type T
     * fromMap the given String keys.
     *
     * @param key  the property's absolute, or relative path, e.g. @code
     *             a/b/c/d.myProperty}.
     * @param type The target type required, not null.
     * @param <T>  the target type.
     * @return the property value, never null..
     * @throws ConfigException if the keys could not be converted to the required target type.
     */
    default <T> Optional<T> getOptional(String key, Class<T> type) {
        return Optional.ofNullable(get(key, type));
    }

    /**
     * Get the property keys as type T. This will implicitly require a corresponding {@link
     * org.apache.tamaya.spi.PropertyConverter} to be available that is capable current providing type T
     * fromMap the given String keys.
     *
     * @param key  the property's absolute, or relative path, e.g. @code
     *             a/b/c/d.myProperty}.
     * @param type The target type required, not null.
     * @param <T>  the target type.
     * @return the property value, never null..
     * @throws ConfigException if the keys could not be converted to the required target type.
     */
    default <T> Optional<T> getOptional(String key, TypeLiteral<T> type) {
        return Optional.ofNullable(get(key, type));
    }

    /**
     * Access all current known Configuration properties as a full {@code Map<String,String>}.
     * Be aware that entries from non scannable parts of the registered {@link org.apache.tamaya.spi.PropertySource}
     * instances may not be contained in the result, but nevertheless be accessible calling one of the
     * {@code get(...)} methods.
     *
     * @return the current properties. These may not include all properties from non-scannable
     * {@link org.apache.tamaya.spi.PropertySource}s.
     */
    @SuppressWarnings("JavaDoc")
    Map<String, String> getProperties();


    /**
     * Get the property keys as {@link Boolean}.
     *
     * @param key the property's absolute, or relative path, e.g. {@code
     *            a/b/c/d.myProperty}.
     * @return the property's keys.
     * @throws ConfigException if the configured value could not be converted to the target type.
     * @since Java8
     */
    default Optional<Boolean> getBoolean(String key) {
        return getOptional(key, Boolean.class);
    }

    /**
     * Get the property keys as {@link Integer}.
     *
     * @param key the property's absolute, or relative path, e.g. @code
     *            a/b/c/d.myProperty}.
     * @return the property's keys.
     * @throws ConfigException if the configured value could not be converted to the target type.
     * @since Java8
     */
    default OptionalInt getInteger(String key) {
        Optional<Integer> val = getOptional(key, Integer.class);

        if (val.isPresent()) {
            return OptionalInt.of(val.get());
        }
        return OptionalInt.empty();
    }

    /**
     * Get the property keys as {@link Long}.
     *
     * @param key the property's absolute, or relative path, e.g. @code
     *            a/b/c/d.myProperty}.
     * @return the property's keys.
     * @throws ConfigException if the configured value could not be converted to the target type.
     * @since Java8
     */
    default OptionalLong getLong(String key) {
        Optional<Long> val = getOptional(key, Long.class);
        if (val.isPresent()) {
            return OptionalLong.of(val.get());
        }
        return OptionalLong.empty();
    }

    /**
     * Get the property keys as {@link Double}.
     *
     * @param key the property's absolute, or relative path, e.g. @code
     *            a/b/c/d.myProperty}.
     * @return the property's keys.
     * @throws ConfigException if the configured value could not be converted to the target type.
     * @since Java8
     */
    default OptionalDouble getDouble(String key) {

        Optional<Double> val = getOptional(key, Double.class);
        if (val.isPresent()) {
            return OptionalDouble.of(val.get());
        }
        return OptionalDouble.empty();
    }

    /**
     * Extension point for adjusting configuration.
     *
     * @param operator A configuration operator, e.g. a filter, or an adjuster
     *                 combining configurations.
     * @return the new adjusted configuration, never {@code null}.
     */
    default Configuration with(ConfigOperator operator) {
        return operator.operate(this);
    }

    /**
     * Query a configuration.
     *
     * @param query the query, never {@code null}.
     * @param <T>       the target query result type.
     * @return the result
     */
    default <T> T query(ConfigQuery<T> query) {
        return query.query(this);
    }

}
