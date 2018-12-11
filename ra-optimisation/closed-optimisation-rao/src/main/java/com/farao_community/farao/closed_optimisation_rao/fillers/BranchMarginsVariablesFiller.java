/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.closed_optimisation_rao.fillers;

import com.farao_community.farao.closed_optimisation_rao.AbstractOptimisationProblemFiller;
import com.google.auto.service.AutoService;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(AbstractOptimisationProblemFiller.class)
public class BranchMarginsVariablesFiller extends AbstractOptimisationProblemFiller {
    private static final String ESTIMATED_FLOW_POSTFIX = "_estimated_flow";
    private static final String ESTIMATED_FLOW_EQUATION_POSTFIX = "_estimated_flow_equation";
    private static final String REFERENCE_FLOWS_DATA_NAME = "reference_flows";

    @Override
    public Map<String, Class> dataExpected() {
        Map<String, Class> returnMap = new HashMap<>();
        returnMap.put(REFERENCE_FLOWS_DATA_NAME, Map.class);
        return returnMap;
    }

    @Override
    public List<String> variablesProvided() {
        List<String> returnList = new ArrayList<>();
        returnList.addAll(cracFile.getPreContingency().getMonitoredBranches().stream()
                .map(branch -> branch.getId() + ESTIMATED_FLOW_POSTFIX).collect(Collectors.toList()));
        returnList.addAll(cracFile.getContingencies().stream()
                .flatMap(contingency -> contingency.getMonitoredBranches().stream())
                .map(branch -> branch.getId() + ESTIMATED_FLOW_POSTFIX).collect(Collectors.toList()));
        return returnList;
    }

    @Override
    public List<String> constraintsProvided() {
        List<String> returnList = new ArrayList<>();
        returnList.addAll(cracFile.getPreContingency().getMonitoredBranches().stream()
                .map(branch -> branch.getId() + ESTIMATED_FLOW_EQUATION_POSTFIX).collect(Collectors.toList()));
        returnList.addAll(cracFile.getContingencies().stream()
                .flatMap(contingency -> contingency.getMonitoredBranches().stream())
                .map(branch -> branch.getId() + ESTIMATED_FLOW_EQUATION_POSTFIX).collect(Collectors.toList()));
        return returnList;
    }

    @Override
    public void fillProblem(MPSolver solver) {
        double infinity = MPSolver.infinity();
        Map<String, Double> referenceFlows = (Map<String, Double>) data.get(REFERENCE_FLOWS_DATA_NAME);

        cracFile.getPreContingency().getMonitoredBranches().forEach(branch -> {
            MPVariable branchFlowVariable = solver.makeNumVar(-infinity, infinity, branch.getId() + ESTIMATED_FLOW_POSTFIX);
            double referenceFlow = referenceFlows.get(branch.getId());
            MPConstraint branchFlowEquation = solver.makeConstraint(referenceFlow, referenceFlow, branch.getId() + ESTIMATED_FLOW_EQUATION_POSTFIX);
            branchFlowEquation.setCoefficient(branchFlowVariable, 1);
        });

        cracFile.getContingencies().stream()
                .flatMap(contingency -> contingency.getMonitoredBranches().stream())
                .forEach(branch -> {
                    MPVariable branchFlowVariable = solver.makeNumVar(-infinity, infinity, branch.getId() + ESTIMATED_FLOW_POSTFIX);
                    double referenceFlow = referenceFlows.get(branch.getId());
                    MPConstraint branchFlowEquation = solver.makeConstraint(referenceFlow, referenceFlow, branch.getId() + ESTIMATED_FLOW_EQUATION_POSTFIX);
                    branchFlowEquation.setCoefficient(branchFlowVariable, 1);
                });
    }
}
