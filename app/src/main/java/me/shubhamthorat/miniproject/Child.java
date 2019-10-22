package me.shubhamthorat.miniproject;

public class Child {
    private String Name;
    private String DOB;

    public Child() {
        //empty constructor needed
    }

    public Child(String Name, String DOB) {
        this.Name = Name;
        this.DOB = DOB;
    }

    public String getName() {
        return Name;
    }

    public String getDOB() {
        return DOB;
    }

}

