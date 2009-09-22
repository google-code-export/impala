/* 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.impalaframework.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;

/**
 * Based on original source code from Apache Avalon: Vertex
 * Vertex is used to track dependencies and each moduleDefinition in a graph.  Typical
 * uses would be to ensure components are started up and torn down in the
 * proper order, or bundles were loaded and unloaded in the proper order, etc.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version CVS $ Revision: 1.1 $
 */
public final class Vertex implements Comparable<Vertex>
{
    private final String name;
    private final ModuleDefinition moduleDefinition;
    private int order;
    
    /** Flag used to keep track of whether or not a given vertex has been
     *   seen by the resolveOrder methods. */
    private boolean seen;
    
    /** List of all direct dependent Vertices. */
    private final List<Vertex> dependencies;

    /**
     * A vertex wraps a moduleDefinition, which can be anything.
     *
     * @param moduleDefinition  The wrapped moduleDefinition.
     */
    public Vertex(final ModuleDefinition node)
    {
        this(node.getName(), node);
    }
    
    /**
     * A vertex wraps a moduleDefinition, which can be anything.
     *
     * @param name  A name for the moduleDefinition which will be used to produce useful errors.
     * @param moduleDefinition  The wrapped moduleDefinition.
     */
    Vertex(final String name)
    {
        this(name, null);
    }

    
    /**
     * A vertex wraps a moduleDefinition, which can be anything.
     *
     * @param name  A name for the moduleDefinition which will be used to produce useful errors.
     * @param moduleDefinition  The wrapped moduleDefinition.
     */
    
    Vertex(final String name, final ModuleDefinition node)
    {
        this.name = name;
        this.moduleDefinition = node;
        this.dependencies = new ArrayList<Vertex>();
        reset();
    }
    
    
    /**
     * Reset the Vertex so that all the flags and runtime states are set back
     * to the original values.
     */
    public void reset()
    {
        this.order = 0;
        this.seen = false;
    }
    
    /**
     * Returns the name of the Vertex.
     *
     * @return The name of the Vertex.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Get the wrapped moduleDefinition that this Vertex represents.
     *
     * @return the moduleDefinition
     */
    public ModuleDefinition getModuleDefinition()
    {
        return moduleDefinition;
    }

    /**
     * Add a dependency to this Vertex.  The Vertex that this one depends on will
     * be marked as referenced and then added to the list of dependencies.  The
     * list is checked before the dependency is added.
     *
     * @param v  The vertex we depend on.
     */
    public void addDependency(Vertex v)
    {
        if ( !dependencies.contains( v ) )
        {
            dependencies.add( v );
        }
    }
    
    
    /**
     * Recurse through the tree from this vertex assigning an order to each
     *  and at the same time checking for any cyclic dependencies.
     *
     * @throws CyclicDependencyException If a cyclic dependency is discovered.
     */
    public void resolveOrder()
        throws CyclicDependencyException
    {
        resolveOrder( getName() );
    }
    
    /**
     * Recursively searches for cycles by travelling down the dependency lists
     *  of this vertex, looking for the start vertex.
     *
     * @param path The path to the Vertex.  It is worth the load as it makes a
     *             descriptive error message possible.
     *
     * @return The highest order of any of the dependent vertices.
     *
     * @throws CyclicDependencyException If a cyclic dependency is discovered.
     */
    private int resolveOrder(String path)
        throws CyclicDependencyException
    {
        seen = true;
        try
        {
            int highOrder = -1;
            for ( Iterator<Vertex> iter = dependencies.iterator(); iter.hasNext(); )
            {
                Vertex dv = iter.next();
                if ( dv.seen )
                {
                    throw new CyclicDependencyException( path + " -> " + dv.getName() );
                }
                else
                {
                    highOrder = Math.max(
                        highOrder, dv.resolveOrder( path + " -> " + dv.getName() ) );
                }
            }
            
            // Set this order so it is one higher than the highest dependency.
            order = highOrder + 1;
            return order;
        }
        finally
        {
            seen = false;
        }
    }

    /**
     * Get the list of dependencies.
     *
     * @return  The list of dependencies.
     */
    public List<Vertex> getDependencies()
    {
        return dependencies;
    }

    /**
     * Used in the sort algorithm to sort all the Vertices so that they respect
     * the ordinal they were given during the topological sort.
     *
     * @param o  The other Vertex to compare with
     * @return -1 if this < o, 0 if this == o, or 1 if this > o
     */
    public int compareTo(final Vertex o)
    {
        final Vertex other = (Vertex) o;
        int orderInd;

        if ( order < other.order )
        {
            orderInd = -1;
        }
        else if ( order > other.order )
        {
            orderInd = 1;
        }
        else
        {
            orderInd = 0;
        }

        return orderInd;
    }

    /**
     * Get the ordinal for this vertex.
     *
     * @return  the order.
     */
    public int getOrder()
    {
        return order;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Vertex name:" + name);
        return buffer.toString();
    }
}
