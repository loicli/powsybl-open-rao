/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.flow_decomposition_results.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Table;

/**
 * FasterXML module for JSON serialization/deserialization
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class FlowDecompositionResultsJsonModule extends SimpleModule {

    public FlowDecompositionResultsJsonModule() {
        addDeserializer(Table.class, new TableDeserializer());
    }

}
