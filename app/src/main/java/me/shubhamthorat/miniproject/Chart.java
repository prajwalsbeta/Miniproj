package me.shubhamthorat.miniproject;

public class Chart {
    private String vName;
    private String vDate;
    public Chart(){}

    public Chart(String vName,String vDate){
        this.vDate=vDate;
        this.vName=vName;
    }
    public String getvName() {
        return vName;
    }

    public String getvDate() {
        return vDate;
    }

}
