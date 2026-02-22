package com.viclovsky.example.oas.client.restassured.junit5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * UI test for Allure report: checks allure-results is not empty,
 * serves the report, opens Behaviors, first test, and verifies step content on the right.
 * Requires Allure CLI on PATH. Run API tests first so allure-results is populated.
 */
class ReportTest {

    private static final int ALLURE_PORT = 19292;
    private static final int SERVER_START_TIMEOUT_SEC = 30;
    private static final String BASE_URL = "http://localhost:" + ALLURE_PORT;

    private Process allureProcess;

    @AfterEach
    void tearDown() {
        if (allureProcess != null && allureProcess.isAlive()) {
            allureProcess.destroyForcibly();
        }
    }

    @Test
    void allureReportShowsBehaviorsAndStepContent() throws IOException, InterruptedException {
        Path resultsDir = getAllureResultsDir();
        assertAllureResultsNotEmpty(resultsDir);

        startAllureServe(resultsDir);
        waitForServerReady();

        open(BASE_URL + "/#behaviors");

        // First test link in the list (Behaviors table: links like "#N testName duration")
        $("a[href*='#behaviors/']").shouldBe(visible);
        $$("a[href*='#behaviors/']").first().click();

        // Each API call must be wrapped in step "Вызов {method} to {endpoint}" with request+response inside
        String bodyText = $("body").getText();
        assertThat(bodyText)
                .as("Step 'Вызов ... to ...' must be present (each call wrapped in Allure step)")
                .contains("Вызов");
        assertThat(bodyText)
                .as("Request attachment must be inside the step")
                .contains("Request");
        assertThat(bodyText)
                .as("Response attachment (HTTP status) must be inside the step")
                .contains("HTTP/1.1");
    }

    private static Path getAllureResultsDir() {
        String dir = System.getProperty("allure.results.directory");
        if (dir != null && !dir.isEmpty()) {
            return Paths.get(dir);
        }
        return Paths.get(System.getProperty("user.dir"), "allure-results");
    }

    private static void assertAllureResultsNotEmpty(Path dir) {
        File folder = dir.toFile();
        Assertions.assertTrue(folder.exists() && folder.isDirectory(),
                "allure-results directory should exist. Run API tests first.");
        File[] files = folder.listFiles();
        Assertions.assertTrue(files != null && files.length > 0,
                "allure-results is empty. Run API tests first.");
    }

    private void startAllureServe(Path resultsDir) throws IOException {
        String path = resultsDir.toAbsolutePath().toString();
        ProcessBuilder pb;
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            pb = new ProcessBuilder("cmd", "/c", "allure", "serve", "--port", String.valueOf(ALLURE_PORT), path);
        } else {
            pb = new ProcessBuilder("allure", "serve", "--port", String.valueOf(ALLURE_PORT), path);
        }
        pb.redirectErrorStream(true);
        pb.inheritIO();
        allureProcess = pb.start();
    }

    private void waitForServerReady() throws InterruptedException {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(SERVER_START_TIMEOUT_SEC);
        while (System.currentTimeMillis() < deadline) {
            try {
                URL url = new URL(BASE_URL + "/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(2000);
                conn.setReadTimeout(2000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                conn.disconnect();
                if (code == 200) {
                    return;
                }
            } catch (IOException ignored) {
                // not ready yet
            }
            Thread.sleep(500);
        }
        if (allureProcess != null && allureProcess.isAlive()) {
            allureProcess.destroyForcibly();
        }
        Assertions.fail("Allure server did not start within " + SERVER_START_TIMEOUT_SEC + " seconds");
    }
}
