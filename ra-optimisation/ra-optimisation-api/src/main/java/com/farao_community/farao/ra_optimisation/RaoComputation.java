/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation;

import java.util.concurrent.CompletableFuture;

/**
 * @author Mohamed Zelmat {@literal <mohamed.zelmat at rte-france.com>}
 */
public interface RaoComputation {
    CompletableFuture<RaoComputationResult> run(String workingStateId, RaoComputationParameters parameters);

}
