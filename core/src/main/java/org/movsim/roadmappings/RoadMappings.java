package org.movsim.roadmappings;

import org.movsim.roadmappings.geometry.RoadGeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

/**
 * Utilities for creating a {@link RoadMapping}.
 * 
 * <br>
 * created: Apr 7, 2013<br>
 * 
 */
public final class RoadMappings {

    private static final Logger LOG = LoggerFactory.getLogger(RoadMappings.class);

    private RoadMappings() {
        throw new IllegalStateException("do not invoke private constructor.");
    }

    /**
     * Factory for creating {@link RoadMapping}.
     * 
     * @param roadGeometry
     * @return
     */
    private static final RoadMapping create(RoadGeometry roadGeometry) {
        RoadMapping roadMapping;
        if (roadGeometry.geometry().isSetLine()) {
            roadMapping = RoadMappingLine.create(roadGeometry);
        } else if (roadGeometry.geometry().isSetArc()) {
            roadMapping = RoadMappingArc.create(roadGeometry);
        } else if (roadGeometry.geometry().isSetPoly3()) {
            throw new IllegalArgumentException("POLY3 geometry not yet supported. ");
        } else if (roadGeometry.geometry().isSetSpiral()) {
            throw new IllegalArgumentException("SPIRAL geometry not yet supported. ");
        } else {
            throw new IllegalArgumentException("Unknown geometry: " + roadGeometry.geometry());
        }
        return roadMapping;
    }

    public static final RoadMapping create(Iterable<RoadGeometry> roadGeometries) {
        Preconditions.checkArgument(!Iterables.isEmpty(roadGeometries));
        if(Iterables.size(roadGeometries) == 1){
            return create(Iterables.getOnlyElement(roadGeometries));
        }
        
        LOG.warn("creating of RoadMappingPoly not well tested..."); // TODO
        RoadGeometry first = Iterables.get(roadGeometries, 0);
        RoadMappingPoly roadMapping = new RoadMappingPoly(first.laneCount(), first.laneWidth());
        for (RoadGeometry roadGeometry : roadGeometries) {
            roadMapping.add(roadGeometry);
        }
        return roadMapping;
    }
}
