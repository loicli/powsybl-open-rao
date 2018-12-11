/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.closed_optimisation_rao;

import com.farao_community.farao.ra_optimisation.RaoComputationParameters;
import com.powsybl.commons.extensions.AbstractExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class ClosedOptimisationRaoParameters extends AbstractExtension<RaoComputationParameters> {
    static final String DEFAULT_SOLVER_TYPE = "GLOP_LINEAR_PROGRAMMING";
    private String solverType = DEFAULT_SOLVER_TYPE;
    private List<String> fillersList = new ArrayList<>();
    List<String> preProcessorsList = new ArrayList<>();
    List<String> postProcessorsList = new ArrayList<>();

    public ClosedOptimisationRaoParameters() {
    }

    public ClosedOptimisationRaoParameters(ClosedOptimisationRaoParameters other) {
        Objects.requireNonNull(other);

        this.solverType = other.solverType;
        this.fillersList.addAll(other.fillersList);
        this.preProcessorsList.addAll(other.preProcessorsList);
        this.postProcessorsList.addAll(other.postProcessorsList);
    }

    public String getSolverType() {
        return this.solverType;
    }

    public ClosedOptimisationRaoParameters setSolverType(String solverType) {
        this.solverType = solverType;
        return this;
    }

    public List<String> getFillersList() {
        return Collections.unmodifiableList(fillersList);
    }

    public ClosedOptimisationRaoParameters addAllFillers(List<String> fillers) {
        this.fillersList.addAll(fillers);
        return this;
    }

    public List<String> getPreProcessorsList() {
        return preProcessorsList;
    }

    public ClosedOptimisationRaoParameters addAllPreProcessors(List<String> preProcessors) {
        this.preProcessorsList.addAll(preProcessors);
        return this;
    }

    public List<String> getPostProcessorsList() {
        return postProcessorsList;
    }

    public ClosedOptimisationRaoParameters addAllPostProcessors(List<String> postProcessors) {
        this.preProcessorsList.addAll(postProcessors);
        return this;
    }

    @Override
    public String getName() {
        return "ClosedOptimisationRaoParameters";
    }
}
