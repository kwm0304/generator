package com.kwm0304.cli.command;

import com.kwm0304.cli.service.GeneratorService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@ShellComponent
public class GeneratorCommand {

    private Path targetDir;
    private Path modelDir;
    private GeneratorService generatorService;
    private String userClass;

    public GeneratorCommand(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @ShellMethod(key = "generate", value = "Generates boilerplate controller, repository, service and optional security files based on path to models directory.")
    public void generate(
            @ShellOption(value = "-l") boolean useLombok,
            @ShellOption(value = "-s") boolean generateSecurity,
            @ShellOption(value = "-p") String modelDirString
    ) {
        if (verifyPath(modelDirString)) {
            modelDir = Paths.get(modelDirString);
            targetDir = modelDir.getParent();
            if (generateSecurity) {
                getUserClass();
            }
            //make directories
            generatorService.makeDirectories(targetDir, generateSecurity);
            //parse model files
            generatorService.parseModelFiles(modelDir, useLombok);

        } else {
            System.out.println("Operation aborted by user.");
        }

        //create files based on model info

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

    public String getUserClass() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("What is the name of your user class?");
        String userInput = scanner.nextLine();
        return userClass = userInput;
    }


}
