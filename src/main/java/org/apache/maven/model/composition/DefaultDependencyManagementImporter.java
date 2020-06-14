package org.apache.maven.model.composition;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelProblemCollector;
import org.codehaus.plexus.component.annotations.Component;

/**
 * Handles the import of dependency management from other models into the target model.
 *
 * @author Benjamin Bentmann
 */
@Component( role = DependencyManagementImporter.class )
public class DefaultDependencyManagementImporter
    implements DependencyManagementImporter
{

    @Override
    public void importManagement( Model target, List<? extends DependencyManagement> sources,
                                  ModelBuildingRequest request, ModelProblemCollector problems )
    {
        if ( sources != null && !sources.isEmpty() )
        {
            Map<String, Dependency> dependencies = new LinkedHashMap<>();

            DependencyManagement depMngt = target.getDependencyManagement();

            if ( depMngt != null )
            {
                for ( Dependency dependency : depMngt.getDependencies() )
                {
                    dependencies.put( dependency.getManagementKey(), dependency );
                }
            }
            else
            {
                depMngt = new DependencyManagement();
                target.setDependencyManagement( depMngt );
            }

            for ( DependencyManagement source : sources )
            {
                for ( Dependency dependency : source.getDependencies() )
                {
                    String key = dependency.getManagementKey();
                    if ( !dependencies.containsKey( key ) )
                    {
                        dependencies.put( key, dependency );
                    }
                }
            }

            depMngt.setDependencies( new ArrayList<>( dependencies.values() ) );
        }
    }

}
