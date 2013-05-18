/*
 * Copyright (C) 2010, 2011, 2012 by Arne Kesting, Martin Treiber, Ralph Germ, Martin Budden
 * <movsim.org@gmail.com>
 * -----------------------------------------------------------------------------------------
 * 
 * This file is part of
 * 
 * MovSim - the multi-model open-source vehicular-traffic simulator.
 * 
 * MovSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MovSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MovSim. If not, see <http://www.gnu.org/licenses/>
 * or <http://www.movsim.org>.
 * 
 * -----------------------------------------------------------------------------------------
 */

package org.movsim.simulator.roadnetwork;

import java.util.Iterator;

import javax.annotation.Nullable;

import org.movsim.simulator.roadnetwork.controller.RoadObjectController;
import org.movsim.simulator.vehicles.Vehicle;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

public class SignalPoint {

    public enum SignalPointType {
        START, END;
    }

    private final SignalPointType type;

    private final double position;

    private final RoadObjectController roadObjectController;

    private final Predicate<Vehicle> vehiclePassedPosition; // TODO measure performance for this lookup

    public SignalPoint(SignalPointType type, double position, RoadObjectController roadObjectController) {
        this.type = type;
        this.roadObjectController = Preconditions.checkNotNull(roadObjectController);
        Preconditions.checkArgument(position >= 0 && position <= roadObjectController.roadSegment().roadLength());
        this.position = position;
        vehiclePassedPosition = new VehiclePassedPosition(position);
    }

    void registerPassingVehicles(double simulationTime, Iterator<Vehicle> passedVehicles) {
        roadObjectController.registerVehicles(type, simulationTime, passedVehicles);
    }

    double position() {
        return position;
    }

    Predicate<Vehicle> predicate() {
        return vehiclePassedPosition;
    }

    private static final class VehiclePassedPosition implements Predicate<Vehicle> {

        private final double position;

        public VehiclePassedPosition(double position) {
            this.position = position;
        }

        @Override
        public boolean apply(@Nullable Vehicle vehicle) {
            if (vehicle == null) {
                return false;
            }
            return vehicle.getFrontPositionOld() <= position && vehicle.getFrontPosition() > position;
        }

    }

}
