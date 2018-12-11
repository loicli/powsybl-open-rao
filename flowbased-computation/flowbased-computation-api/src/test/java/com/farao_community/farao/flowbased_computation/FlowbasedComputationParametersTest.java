/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flowbased_computation;

import com.google.auto.service.AutoService;
import com.powsybl.commons.config.PlatformConfig;
import com.powsybl.commons.extensions.AbstractExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Di Gallo Luc  {@literal <luc.di-gallo at rte-france.com>}
 */
public class FlowbasedComputationParametersTest {

    protected PlatformConfig config;

    @Before
    public void setUp() {
        config = Mockito.mock(PlatformConfig.class);
    }

    @Test
    public void testExtensions() {
        FlowBasedComputationParameters parameters = new FlowBasedComputationParameters();
        DummyExtension dummyExtension = new DummyExtension();
        parameters.addExtension(DummyExtension.class, dummyExtension);

        assertEquals(1, parameters.getExtensions().size());
        assertEquals(true, parameters.getExtensions().contains(dummyExtension));
        assertEquals(true, parameters.getExtensionByName("dummyExtension") instanceof DummyExtension);
        assertEquals(true, parameters.getExtension(DummyExtension.class) instanceof DummyExtension);
    }

    @Test
    public void testNoExtensions() {
        FlowBasedComputationParameters parameters = new FlowBasedComputationParameters();

        assertEquals(0, parameters.getExtensions().size());
        assertEquals(false, parameters.getExtensions().contains(new DummyExtension()));
        assertEquals(false, parameters.getExtensionByName("dummyExtension") instanceof DummyExtension);
        assertEquals(false, parameters.getExtension(DummyExtension.class) instanceof DummyExtension);
    }

    @Test
    public void testExtensionFromConfig() {
        FlowBasedComputationParameters parameters = FlowBasedComputationParameters.load(config);

        assertEquals(1, parameters.getExtensions().size());
        assertEquals(true, parameters.getExtensionByName("dummyExtension") instanceof DummyExtension);
        assertNotNull(parameters.getExtension(DummyExtension.class));
    }

    private static class DummyExtension extends AbstractExtension<FlowBasedComputationParameters> {

        @Override
        public String getName() {
            return "dummyExtension";
        }
    }

    @AutoService(FlowBasedComputationParameters.ConfigLoader.class)
    public static class DummyLoader implements FlowBasedComputationParameters.ConfigLoader<DummyExtension> {

        @Override
        public DummyExtension load(PlatformConfig platformConfig) {
            return new DummyExtension();
        }

        @Override
        public String getExtensionName() {
            return "dummyExtension";
        }

        @Override
        public String getCategoryName() {
            return "fb-computation-parameters";
        }

        @Override
        public Class<? super DummyExtension> getExtensionClass() {
            return DummyExtension.class;
        }
    }
}
