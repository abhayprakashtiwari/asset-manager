package in.apt.assetmanager.constants;

public enum  Status {

    ACTIVE("ACTIVE"), ARCHIVED("ARCHIVED");

    String status;

    Status(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
