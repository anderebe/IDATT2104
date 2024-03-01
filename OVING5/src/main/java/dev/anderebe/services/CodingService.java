package dev.anderebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CodingService {

    private static final Logger logger = LoggerFactory.getLogger(CodingService.class);

    public static String run(String code, String language) throws IOException, InterruptedException {
        String fileName = "";
        String dockerImg = "";
        String dockCode = "";
        String output = "";

        switch (language) {
            case "Java" -> {
                logger.info("Compiling Java code...");
                fileName = "java.java";
                dockerImg = "java-Docker";
                dockCode = "java:latest";
            }
            case "Python" -> {
                logger.info("Compiling Python code...");
                fileName = "python.py";
                dockerImg = "Python-Docker";
                dockCode = "python:latest";
            }
            case "C" -> {
                logger.info("Compiling C code...");
                fileName = "c.c";
                dockerImg = "C-Docker";
            }
            case "CPP" -> {
                logger.info("Compiling C++ code...");
                fileName = "cpp.cpp";
                dockerImg = "CPP-Docker";
            }
            default -> {
                logger.error("Unsupported language: " + language);
                return "Unsupported language: " + language;
            }
        }
        logger.info("Writing code to file...");
        writeCodeToFile(code, fileName);
        logger.info("Code written to file!");
        logger.info("Building docker image...");
        buildDockIMG(dockerImg, fileName, language);
        logger.info("Docker image built!");

        output = CompilePython(code, language, dockCode, fileName, dockerImg);

        removeIMG(dockerImg);

        return output;
    }

    private static void buildDockIMG(String dockerImg, String fileName, String language) {
        logger.info("Building docker image...");
        String[] dockerBuild = {"docker", "build", "-t", dockerImg, "-f", language + ".Dockerfile", "."};
        logger.info("Build::" + Arrays.toString(dockerBuild));
        ProcessBuilder dockerBuildProcessBuilder = new ProcessBuilder(dockerBuild);
        dockerBuildProcessBuilder.directory(new File("./src/backend/resources/docker/"));
        logger.info("Build complete!");
    }

    private static void removeIMG(String dockerImg) throws IOException, InterruptedException {
        String[] dockerRemoveImage = {"docker", "image", "rm", dockerImg};
        ProcessBuilder dockerRemoveImageBuilder = new ProcessBuilder(dockerRemoveImage);
        Process dockerRemove = dockerRemoveImageBuilder.start();
        dockerRemove.waitFor();
    }

    private static String CompileCode(String code, String language, String dockCode, String fileName, String dockerImg) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CompileCode'");
    }

    private static String CompilePython(String code, String language, String dockCode, String fileName, String dockerImg) {
        try{
            code = code.replace("\"", "'");

            // Run Docker image
            String[] dockerCommand = {"docker", "run", "--rm", dockCode, language.toLowerCase(), "-c", code};
            Process dockerProcess = Runtime.getRuntime().exec(dockerCommand);

            // Read output
            StringBuilder output = outputLogs(dockerProcess);

            // Read potential error messages
            StringBuilder errors = errorLogs(dockerProcess);
            if (!errors.isEmpty()) {
                logger.error("Error encountered while compiling and running the Python source code. " +
                        "Adding error to end of output.");
                output.append(errors);
            }
            dockerProcess.waitFor();

            return output.toString();

        } catch (Exception e) {
            logger.error("Error compiling " + language + ":" + e.getMessage());
            return "Error compiling " + language + " code: " + e.getMessage();
        }
    }

    private static void writeCodeToFile(String sourceCode, String fileName) throws IOException{
        FileWriter writer = new FileWriter("src/main/resources/docker/" + fileName);
        writer.write(sourceCode);
        writer.close();
    }

    private static StringBuilder outputLogs(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output;
    }

    private static StringBuilder errorLogs(Process process) throws IOException {
        StringBuilder errors = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            errors.append(line).append("\n");
        }
        return errors;
    }
}
