package org.apache.maven.model.superpom;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * Provides the super POM that all models implicitly inherit from.
 *
 * @author Benjamin Bentmann
 */
@Component( role = SuperPomProvider.class )
public class DefaultSuperPomProvider
    implements SuperPomProvider
{

    /**
     * The cached super POM, lazily created.
     */
    private Model superModel;

    @Requirement
    private ModelProcessor modelProcessor;

    public DefaultSuperPomProvider setModelProcessor( ModelProcessor modelProcessor )
    {
        this.modelProcessor = modelProcessor;
        return this;
    }

    @Override
    public Model getSuperModel( String version )
    {
        if ( superModel == null )
        {
            String resource = "pom-" + version + ".xml";

            InputStream is;
            try {
                is = Optional.ofNullable(getClass().getResourceAsStream(resource))
                    .orElse(getClass().getModule().getResourceAsStream( resource ));
            } catch (IOException e1) {
                is = null;
            }

            if ( is == null )
            {
                throw new IllegalStateException( "The super POM " + resource + " was not found"
                    + ", please verify the integrity of your Maven installation" );
            }

            try
            {
                Map<String, String> options = new HashMap<>();
                options.put( "xml:4.0.0", "xml:4.0.0" );
                superModel = modelProcessor.read( is, options );
            }
            catch ( IOException e )
            {
                throw new IllegalStateException( "The super POM " + resource + " is damaged"
                    + ", please verify the integrity of your Maven installation", e );
            }
        }

        return superModel;
    }

}
