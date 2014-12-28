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

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.*;

/**
 * Event that contains a set current changes that were applied or could be applied.
 * This class is immutable and thread-safe. To create instances use
 * {@link ConfigChangeSetBuilder}.
 *
 * Created by Anatole on 22.10.2014.
 */
public final class ConfigChangeSet implements Serializable{

    private static final long serialVersionUID = 1l;
    /** The base property provider/configuration. */
    private PropertySource propertySource;
    /** The base version, usable for optimistic locking. */
    private String baseVersion;
    /** The recorded changes. */
    private Map<String,PropertyChangeEvent> changes = new HashMap<>();

    /**
     * Get an empty change set for the given provider.
     * @param propertyProvider The base property provider/configuration, not null.
     * @return an empty ConfigChangeSet instance.
     */
    public static ConfigChangeSet emptyChangeSet(PropertySource propertyProvider){
        return new ConfigChangeSet(propertyProvider, Collections.emptySet());
    }

    /**
     * Constructor used by {@link ConfigChangeSetBuilder}.
     * @param propertySource The base property provider/configuration, not null.
     * @param changes The recorded changes, not null.
     */
    ConfigChangeSet(PropertySource propertySource, Collection<PropertyChangeEvent> changes) {
        this.propertySource = Objects.requireNonNull(propertySource);
        changes.forEach((c) -> this.changes.put(c.getPropertyName(), c));
    }

    /**
     * Get the underlying property provider/configuration.
     * @return the underlying property provider/configuration, never null.
     */
    public PropertySource getPropertySource(){
        return this.propertySource;
    }

    /**
     * Get the base version, usable for optimistic locking.
     * @return the base version.
     */
    public String getBaseVersion(){
        return baseVersion;
    }

    /**
     * Get the changes recorded.
     * @return the recorded changes, never null.
     */
    public Collection<PropertyChangeEvent> getEvents(){
        return Collections.unmodifiableCollection(this.changes.values());
    }

    /**
     * Access the number current removed entries.
     * @return the number current removed entries.
     */
    public int getRemovedSize() {
        return (int) this.changes.values().stream().filter((e) -> e.getNewValue() == null).count();
    }

    /**
     * Access the number current added entries.
     * @return the number current added entries.
     */
    public int getAddedSize() {
        return (int) this.changes.values().stream().filter((e) -> e.getOldValue() == null).count();
    }

    /**
     * Access the number current updated entries.
     * @return the number current updated entries.
     */
    public int getUpdatedSize() {
        return (int) this.changes.values().stream().filter((e) -> e.getOldValue()!=null && e.getNewValue()!=null).count();
    }


    /**
     * Checks if the given key was removed.
     * @param key the target key, not null.
     * @return true, if the given key was removed.
     */
    public boolean isRemoved(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getNewValue() == null;
    }

    /**
     * Checks if the given key was added.
     * @param key the target key, not null.
     * @return true, if the given key was added.
     */
    public boolean isAdded(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getOldValue() == null;
    }

    /**
     * Checks if the given key was updated.
     * @param key the target key, not null.
     * @return true, if the given key was updated.
     */
    public boolean isUpdated(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getOldValue() != null && change.getNewValue() != null;
    }

    /**
     * Checks if the given key is added, or updated AND NOT removed.
     * @param key the target key, not null.
     * @return true, if the given key was added, or updated BUT NOT removed.
     */
    public boolean containsKey(String key) {
        PropertyChangeEvent change = this.changes.get(key);
        return change != null && change.getNewValue() != null;
    }

    /**
     * CHecks if the current change set does not contain any changes.
     * @return tru, if the change set is empty.
     */
    public boolean isEmpty(){
        return this.changes.isEmpty();
    }


    @Override
    public String toString() {
        return "ConfigChangeSet{" +
                "properties=" + propertySource +
                ", baseVersion=" + baseVersion +
                ", changes=" + changes +
                '}';
    }
}
