package com.kwm0304.cli.command;

import com.kwm0304.cli.GeneratorConfig;
import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.service.GeneratorService;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@ShellComponent
public class GeneratorCommand {
    @Autowired
    private GeneratorService generatorService;
    private final GeneratorConfig generatorConfig;
    private List<String> fileNames = new ArrayList<>();

    public GeneratorCommand(GeneratorService generatorService, GeneratorConfig generatorConfig) {
        this.generatorService = generatorService;
        this.generatorConfig = generatorConfig;
    }

    @ShellMethod(key = "generate", value = "Generates boilerplate controller, repository, service and optional security files based on path to models directory.")
    public void generate(
            @ShellOption(value = "-l") boolean useLombok,
            @ShellOption(value = "-s") boolean generateSecurity,
            @ShellOption(value = "-p") String modelDirString
    ) {
        generatorConfig.setModelDirString(modelDirString);
        if (verifyPath()) {
            generatorConfig.setModelDir(Paths.get(modelDirString));
            generatorConfig.setTargetDir((generatorConfig.getModelDir()).getParent());
            generatorConfig.setUseLombok(useLombok);
            generatorConfig.setGenerateSecurity(generateSecurity);
            if (generateSecurity) {
                getUserClass();
            }
            //make directories
            generatorService.makeDirectories();
            //parse model files
            generatorService.parseModelFiles();

        } else {
            System.out.println("Operation aborted by user.");
        }

        //create files based on model info

    }

    public boolean verifyPath() {
        Scanner scanner = new Scanner(System.in);
        File directory = new File(generatorConfig.getModelDirString());
        File[] fileList = directory.listFiles();

        if (fileList != null & fileList.length > 0) {
            System.out.println("Files found in directory:");
            for (File file : fileList) {
                System.out.println(file.getName());
                String cleanFile = file.getName().substring(0, file.getName().length() - 5);
                fileNames.add(cleanFile);
                //ask user if this is correct path, if so continue, if not, exit
            }
            System.out.print("Is this the correct path? (yes/no): ");
            String userInput = scanner.nextLine();
            if ("no".equalsIgnoreCase(userInput) || "n".equalsIgnoreCase(userInput)) {
                System.out.println("Incorrect directory chosen.");
                return false;
            }
            return ("yes".equalsIgnoreCase(userInput) || "y".equalsIgnoreCase(userInput));
        } else {
            System.out.println("The directory is empty or does not exist.");
            return false;
        }
    }

    public void getUserClass() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("What is the name of your user class?");
            String userInput = scanner.nextLine();
            if (isFilePresent(userInput)) {
                generatorConfig.setUserClass(StringUtils.cleanClassName(userInput));
                break;
            } else {
                System.out.println("No class with this name was found.");
            }
        }
    }

    public boolean isFilePresent(String input) {
        return fileNames.stream().anyMatch(fileName -> fileName.equalsIgnoreCase(input));
    }
}
