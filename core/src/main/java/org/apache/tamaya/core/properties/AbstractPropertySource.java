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
package org.apache.tamaya.core.properties;

import java.io.Serializable;
import java.util.*;

import org.apache.tamaya.ConfigChangeSet;
import org.apache.tamaya.PropertySource;
import org.apache.tamaya.spi.ConfigChangeSetCallback;

/**
 * Abstract base class for implementing a {@link org.apache.tamaya.PropertySource}.
 */
public abstract class AbstractPropertySource implements PropertySource, Serializable{
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6553955893879292837L;

    protected String name;

    /**
     * The underlying sources.
     */
    private volatile Set<String> sources = new HashSet<>();

    private volatile Set<ConfigChangeSetCallback> callbacks = new LinkedHashSet<>();

    /**
     * Constructor.
     */
    protected AbstractPropertySource(String name){
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName(){
        return name;
    }


    /**
     * Method that allows an additional source to be added, to be used by
     * subclasses.
     *
     * @param source the source, not {@code null}.
     */
    protected void addSource(String source){
        Objects.requireNonNull(source);
        this.sources.add(source);
    }


    protected void addSources(Collection<String> sources){
        Objects.requireNonNull(sources);
        this.sources.addAll(sources);
    }

    @Override
    public Optional<String> get(String key){
        return Optional.ofNullable(getProperties().get(key));
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder(getClass().getSimpleName()).append("{\n");
        b.append("  ").append("(").append(getName()).append(" = ").append(getName()).append(")\n");
        printContents(b);
        return b.append('}').toString();
    }

    @Override
    public void update(ConfigChangeSet changeSet) {
        //TODO how do we want to update this guy?
        this.callbacks.parallelStream().forEach((c) -> c.onChange(changeSet));
    }

    @Override
    public void registerForUpdate(ConfigChangeSetCallback callback) {
        this.callbacks.add(callback);
    }

    @Override
    public void removeForUpdate(ConfigChangeSetCallback callback) {
        this.callbacks.remove(callback);
    }

    protected String printContents(StringBuilder b){
        Map<String,String> sortMap = getProperties();
        if(!(sortMap instanceof SortedMap)){
            sortMap = new TreeMap<>(sortMap);
        }
        for(Map.Entry<String,String> en : sortMap.entrySet()){
            b.append("  ").append(en.getKey()).append(" = \"").append(en.getValue().replaceAll("\\\"", "\\\\\"").replaceAll("=", "\\=")).append("\"\n");
        }
        return b.toString();
    }

}
