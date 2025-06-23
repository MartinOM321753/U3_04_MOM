package mx.edu.utez.U3_04_OMM.DTO;

public class ResponseDTO {

    private int numErrors;
    private  String message;

    public int getNumErrors() {
        return numErrors;
    }

    public void setNumErrors(int numErrors) {
        this.numErrors = numErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
