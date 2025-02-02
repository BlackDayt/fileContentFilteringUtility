package dev.utils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Context {
    private final List<String> inputFiles = new ArrayList<>();
    private String prefix = "";
    private String outputPath = Paths.get("").toAbsolutePath().toString();
    private boolean isFullStatistic = false;
    private boolean appendMode = false;
    private boolean printInfo = false;
    
    public void addInputFile(String file) {
        inputFiles.add(file);
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setFullStatistic(boolean fullStatistic) {
        isFullStatistic = fullStatistic;
    }

    public boolean isFullStatistic() {
        return isFullStatistic;
    }

    public void setAppendMode(boolean appendMode) {
        this.appendMode = appendMode;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public boolean isPrintInfo() {
        return printInfo;
    }

    public void setPrintInfo(boolean printInfo) {
        this.printInfo = printInfo;
    }
}
