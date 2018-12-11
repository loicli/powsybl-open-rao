/**
 * Copyright (c) 2018, All partners of the iTesla project (http://www.itesla-project.eu/consortium)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.powsybl.commons.extensions.Extension;
import com.powsybl.commons.extensions.ExtensionJsonSerializer;
import com.powsybl.commons.extensions.ExtensionProviders;
import com.powsybl.commons.json.JsonUtil;
import com.farao_community.farao.ra_optimisation.RaoComputationParameters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Provides methods to read and write JsonRaoComputationParameters from and to JSON.
 *
 * @author Mohamed Zelmat {@literal <mohamed.zelmat at rte-france.com>}
 */
public final class JsonRaoComputationParameters {

    /**
     * A configuration loader interface for the LoadFlowParameters extensions loaded from the platform configuration
     *
     * @param <E> The extension class
     */
    public interface ExtensionSerializer<E extends Extension<RaoComputationParameters>> extends ExtensionJsonSerializer<RaoComputationParameters, E> {
    }

    /**
     * Lazily initialized list of extension serializers.
     */
    private static final Supplier<ExtensionProviders<ExtensionSerializer>> SUPPLIER =
            Suppliers.memoize(() -> ExtensionProviders.createProvider(ExtensionSerializer.class, "rao-computation-parameters"));

    /**
     * Gets the known extension serializers.
     */
    public static ExtensionProviders<ExtensionSerializer> getExtensionSerializers() {
        return SUPPLIER.get();
    }

    private JsonRaoComputationParameters() {
    }

    /**
     * Reads parameters from a JSON file (will NOT rely on platform config).
     */
    public static RaoComputationParameters read(Path jsonFile) {
        return update(new RaoComputationParameters(), jsonFile);
    }

    /**
     * Reads parameters from a JSON file (will NOT rely on platform config).
     */
    public static RaoComputationParameters read(InputStream jsonStream) {
        return update(new RaoComputationParameters(), jsonStream);
    }

    /**
     * Updates parameters by reading the content of a JSON file.
     */
    public static RaoComputationParameters update(RaoComputationParameters parameters, Path jsonFile) {
        Objects.requireNonNull(jsonFile);

        try (InputStream is = Files.newInputStream(jsonFile)) {
            return update(parameters, is);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Updates parameters by reading the content of a JSON stream.
     */
    public static RaoComputationParameters update(RaoComputationParameters parameters, InputStream jsonStream) {
        try {
            ObjectMapper objectMapper = createObjectMapper();
            return objectMapper.readerForUpdating(parameters).readValue(jsonStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes parameters as JSON to a file.
     */
    public static void write(RaoComputationParameters parameters, Path jsonFile) {
        Objects.requireNonNull(jsonFile);

        try (OutputStream outputStream = Files.newOutputStream(jsonFile)) {
            write(parameters, outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes parameters as JSON to an output stream.
     */
    public static void write(RaoComputationParameters parameters, OutputStream outputStream) {
        try {
            ObjectMapper objectMapper = createObjectMapper();
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            writer.writeValue(outputStream, parameters);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Low level deserialization method, to be used for instance for reading rao computation parameters nested in another object.
     */
    public static RaoComputationParameters deserialize(JsonParser parser, DeserializationContext context, RaoComputationParameters parameters) throws IOException {
        return new RaoComputationParametersDeserializer().deserialize(parser, context, parameters);
    }

    /**
     * Low level deserialization method, to be used for instance for updating rao computation parameters nested in another object.
     */
    public static RaoComputationParameters deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return new RaoComputationParametersDeserializer().deserialize(parser, context);
    }

    /**
     * Low level serialization method, to be used for instance for writing rao computation parameters nested in another object.
     */
    public static void serialize(RaoComputationParameters parameters, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        new RaoComputationParametersSerializer().serialize(parameters, jsonGenerator, serializerProvider);
    }

    private static ObjectMapper createObjectMapper() {
        return JsonUtil.createObjectMapper()
                .registerModule(new RaoComputationParametersJsonModule());
    }
}
