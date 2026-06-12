package com.studentms.model;

public class Department {
    private int deptId;
    private String deptName;
    private String deptHead;

    public Department() {}

    public Department(int deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getDeptHead() { return deptHead; }
    public void setDeptHead(String deptHead) { this.deptHead = deptHead; }

    @Override
    public String toString() { return deptName; }
}
