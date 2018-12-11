/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.xlsx.validator;

/**
 * Empty Validater class
 */
public final class NoopValidater<T> implements Validater<T> {
    @Override
    public void validate(T value, int row) {

    }
}
