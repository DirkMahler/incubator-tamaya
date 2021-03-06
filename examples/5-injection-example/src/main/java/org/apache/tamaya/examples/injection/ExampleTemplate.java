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
package org.apache.tamaya.examples.injection;

import org.apache.tamaya.inject.ConfigRoot;
import org.apache.tamaya.inject.ConfiguredProperty;
import org.apache.tamaya.inject.DefaultValue;

/**
 * Simple example bean, mapped by default names mostly.
 */
@ConfigRoot("example")
public interface ExampleTemplate {

    public String getType();
    public String getName();
    @DefaultValue("No description available.")
    public String getDescription();
    public int getVersion();
    @ConfiguredProperty(keys = "author")
    public String getExampleAuthor();

}
