package com.kwm0304.cli.command;

import com.kwm0304.cli.service.GeneratorService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@ShellComponent
public class GeneratorCommand {

    private Path targetDir;
    private Path modelDir;
    private Path serviceDir;
    private Path controllerDir;
    private Path repositoryDir;
    private Path securityDir;
    private GeneratorService generatorService;

    @ShellMethod(key = "generate", value = "Generates boilerplate controller, repository, service and optional security files based on path to models directory.")
    public void generate(
            @ShellOption(value = "-l") boolean useLombok,
            @ShellOption(value = "-s") boolean generateSecurity,
            @ShellOption(value = "-p") String modelDirString
    ) {
        if (verifyPath(modelDirString)) {
            modelDir = Paths.get(modelDirString);
            targetDir = modelDir.getParent();

            //make directories
            makeDirectories(targetDir, generateSecurity);

        } else {
            System.out.println("Operation aborted by user.");
        }
        //parseModelFile
        //create files based on model info

    }

    private void makeDirectories(Path targetDir, boolean generateSecurity) {
        if (targetDir == null) {
            System.err.println("Target directory cannot be null");
            return;
        }
        try {
            serviceDir = targetDir.resolve("service");
            controllerDir = targetDir.resolve("controller");
            repositoryDir = targetDir.resolve("repository");

            if (generateSecurity) {
                securityDir = targetDir.resolve("security");
                Files.createDirectories(securityDir);
            }

            Files.createDirectories(serviceDir);
            Files.createDirectories(controllerDir);
            Files.createDirectories(repositoryDir);
        } catch (IOException e) {
            System.err.println("Failed to create directories: " + e.getMessage());
        }
    }

    public boolean verifyPath(String modelDirString) {
        Scanner scanner = new Scanner(System.in);
        File directory = new File(modelDirString);
        File[] fileList = directory.listFiles();

        if (fileList != null & fileList.length > 0) {
            System.out.println("Files found in directory:");
            for (File file : fileList) {
                System.out.println(file.getName());
                //ask user if this is correct path, if so continue, if not, exit
            }
            System.out.print("Is this the correct path? (yes/no): ");
            String userInput = scanner.nextLine();
            return "yes".equalsIgnoreCase(userInput);
        } else {
            System.out.println("The directory is empty or does not exist.");
            return false;
        }
    }
}
