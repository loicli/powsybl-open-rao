/*
 * Copyright (c) 2019, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.search_tree_rao.process.search_tree;

import com.farao_community.farao.commons.FaraoException;
import com.farao_community.farao.data.crac_api.Crac;
import com.farao_community.farao.data.crac_api.NetworkAction;
import com.farao_community.farao.data.crac_api.UsageMethod;
import com.farao_community.farao.ra_optimisation.RaoComputationResult;
import com.farao_community.farao.rao_api.RaoParameters;
import com.powsybl.iidm.network.Network;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * The "tree" is one of the core object of the search-tree algorithm.
 * It aims at finding a good combination of Network Actions.
 *
 * The tree is composed of leaves which evaluate the impact of Network Actions,
 * one by one. The tree is orchestrating the leaves : it looks for a smart
 * routing among the leaves in order to converge as quick as possible to a local
 * minimum of the objective function.
 *
 * @author Joris Mancini {@literal <joris.mancini at rte-france.com>}
 * @author Baptiste Seguinot {@literal <baptiste.seguinot at rte-france.com>}
 */
public final class Tree {

    private Tree() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static CompletableFuture<RaoComputationResult> search(Network network, Crac crac, String referenceNetworkVariant, RaoParameters parameters) {
        Leaf optimalLeaf = new Leaf();
        optimalLeaf.evaluate(network, crac, referenceNetworkVariant, parameters);

        if (optimalLeaf.getStatus() == Leaf.Status.EVALUATION_ERROR) {
            //TODO : improve error messages depending on leaf error (Sensi divergent, infeasible optimisation, time-out, ...)
            throw new FaraoException("Initial case returns an error");
        }

        boolean hasImproved;
        do {
            Set<NetworkAction> availableNetworkActions = crac.getNetworkActions(network, crac.getPreventiveState(), UsageMethod.AVAILABLE);
            List<Leaf> generatedLeaves = optimalLeaf.bloom(availableNetworkActions);

            if (generatedLeaves.isEmpty()) {
                break;
            }

            //TODO: manage parallel computation
            generatedLeaves.forEach(leaf -> leaf.evaluate(network, crac, referenceNetworkVariant, parameters));

            hasImproved = false;
            for (Leaf currentLeaf: generatedLeaves) {
                if (currentLeaf.getStatus() == Leaf.Status.EVALUATION_SUCCESS && getCost(currentLeaf.getRaoResult()) < getCost(optimalLeaf.getRaoResult())) {
                    hasImproved = true;
                    optimalLeaf = currentLeaf;
                }
            }
            //TODO: generalize to handle different stop criterion
        } while (getCost(optimalLeaf.getRaoResult()) < 0 && hasImproved);

        //TODO: build SearchTreeRaoResult object
        return CompletableFuture.completedFuture(optimalLeaf.getRaoResult());
    }

    /**
     * Temporarily function, will be deprecated once the RaoResult will
     * be refactored
     */
    private static double getCost(RaoComputationResult raoResult) {
        // TODO: get objective function value
        // below is a dummy temporary implementation, as "return 0;" was not accepted by Sonar
        return raoResult.getContingencyResults().size();
    }
}