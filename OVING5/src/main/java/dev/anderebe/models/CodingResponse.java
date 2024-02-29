package dev.anderebe.models;

public class CodingResponse {
    private String code;
    private String selectedlanguage;
    private String output;

    public CodingResponse(String code, String selectedlanguage, String output) {
        this.code = code;
        this.selectedlanguage = selectedlanguage;
        this.output = output;
    }

    public String getCode() {
        return code;
    }

    public String getSelectedlanguage() {
        return selectedlanguage;
    }

    public String getOutput() {
        return output;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSelectedlanguage(String selectedlanguage) {
        this.selectedlanguage = selectedlanguage;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    
}
