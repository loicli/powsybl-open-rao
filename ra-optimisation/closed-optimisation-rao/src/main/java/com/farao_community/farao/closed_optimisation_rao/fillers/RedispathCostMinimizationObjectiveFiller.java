/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.closed_optimisation_rao.fillers;

import com.farao_community.farao.closed_optimisation_rao.AbstractOptimisationProblemFiller;
import com.google.auto.service.AutoService;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(AbstractOptimisationProblemFiller.class)
public class RedispathCostMinimizationObjectiveFiller extends AbstractOptimisationProblemFiller {
    private static final String TOTAL_REDISPATCH_COST = "total_redispatch_cost";

    @Override
    public List<String> objectiveFunctionsProvided() {
        return Collections.singletonList("total_redispatch_cost_minimization");
    }

    @Override
    public List<String> variablesExpected() {
        return Collections.singletonList(TOTAL_REDISPATCH_COST);
    }

    @Override
    public void fillProblem(MPSolver solver) {
        MPVariable totalRedispatchCost = Objects.requireNonNull(solver.lookupVariableOrNull(TOTAL_REDISPATCH_COST));
        MPObjective objective = solver.objective();
        // In case previous objective has been set, clear
        objective.clear();
        objective.setCoefficient(totalRedispatchCost, 1);
        objective.setMinimization();
    }
}
