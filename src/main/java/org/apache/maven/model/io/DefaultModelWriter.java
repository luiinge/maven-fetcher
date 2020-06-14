package org.apache.maven.model.io;

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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.WriterFactory;

/**
 * Handles serialization of a model into some kind of textual format like XML.
 *
 * @author Benjamin Bentmann
 */
@Component( role = ModelWriter.class )
public class DefaultModelWriter
    implements ModelWriter
{

    @Override
    public void write( File output, Map<String, Object> options, Model model )
        throws IOException
    {
        Validate.notNull( output, "output cannot be null" );
        Validate.notNull( model, "model cannot be null" );

        output.getParentFile().mkdirs();

        write( WriterFactory.newXmlWriter( output ), options, model );
    }

    @Override
    public void write( Writer output, Map<String, Object> options, Model model )
        throws IOException
    {
        Validate.notNull( output, "output cannot be null" );
        Validate.notNull( model, "model cannot be null" );

        try
        {
            MavenXpp3Writer w = new MavenXpp3Writer();
            w.write( output, model );
        }
        finally
        {
            IOUtil.close( output );
        }
    }

    @Override
    public void write( OutputStream output, Map<String, Object> options, Model model )
        throws IOException
    {
        Validate.notNull( output, "output cannot be null" );
        Validate.notNull( model, "model cannot be null" );

        try
        {
            String encoding = model.getModelEncoding();
            // TODO Use StringUtils here
            if ( encoding == null || encoding.length() <= 0 )
            {
                encoding = "UTF-8";
            }
            write( new OutputStreamWriter( output, encoding ), options, model );
        }
        finally
        {
            IOUtil.close( output );
        }
    }

}
