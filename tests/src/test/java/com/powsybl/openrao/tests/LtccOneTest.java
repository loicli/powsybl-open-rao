package com.powsybl.openrao.tests;

import com.powsybl.iidm.network.Network;
import com.powsybl.openrao.data.cracapi.Crac;
import com.powsybl.openrao.data.cracapi.parameters.CracCreationParameters;
import com.powsybl.openrao.data.raoresultapi.RaoResult;
import com.powsybl.openrao.raoapi.RaoInput;
import com.powsybl.openrao.raoapi.json.JsonRaoParameters;
import com.powsybl.openrao.raoapi.parameters.RangeActionsOptimizationParameters;
import com.powsybl.openrao.raoapi.parameters.RaoParameters;
import com.powsybl.openrao.searchtreerao.castor.algorithm.Castor;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class LtccOneTest {

    static String border = "ES_FR";
    static RangeActionsOptimizationParameters.Solver solver = RangeActionsOptimizationParameters.Solver.KNITRO;
    static String specific_parameter = "KN_PARAM_PRESOLVE 0 ";

    @Test
    void ltccTest() throws Exception {
        Path base = Path.of("src", "test", "resources", "files", "ltcc", border);
        Network network = importNetwork(base.resolve("InitialState.xiidm").toUri().toString());
        Crac crac = importCrac(base.resolve("crac.json").toUri().toString(), network);
        RaoParameters raoParameters = importRaoParameters(base.resolve("raoParameters.json").toUri().toString());
        raoParameters.getRangeActionsOptimizationParameters().getLinearOptimizationSolver().setSolver(solver);
        raoParameters.getRangeActionsOptimizationParameters().getLinearOptimizationSolver().setSolverSpecificParameters(specific_parameter);
        Castor castor = new Castor();
        CompletableFuture<RaoResult> raoResult = castor.run(RaoInput.build(network, crac).build(), raoParameters, null);
        System.out.println(raoResult.get().getComputationStatus());
    }

    private static Network importNetwork(String networkFileUrl) throws IOException {
        return Network.read(getFileNameFromUrl(networkFileUrl), openUrlStream(networkFileUrl));
    }

    private static Crac importCrac(String cracFileUrl, Network network) throws IOException {
        return Crac.read(getFileNameFromUrl(cracFileUrl), openUrlStream(cracFileUrl), network, null, new CracCreationParameters());
    }

    private static String getFileNameFromUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return FilenameUtils.getName(url.getPath());
    }

    private static InputStream openUrlStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        return url.openStream(); // NOSONAR Usage of whitelist not triggered by Sonar quality assessment, even if listed as a solution to the vulnerability
    }

    private static RaoParameters importRaoParameters(String raoParametersFileUrl) throws IOException {
        //keep using update method instead of read directly to avoid serialisation issues
        RaoParameters defaultRaoParameters = new RaoParameters();
        InputStream customRaoParameters = openUrlStream(raoParametersFileUrl);
        return JsonRaoParameters.update(defaultRaoParameters, customRaoParameters);
    }

}
