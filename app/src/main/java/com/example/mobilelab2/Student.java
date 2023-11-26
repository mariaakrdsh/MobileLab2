package com.example.mobilelab2;

public class Student {

    private int id;
    private String pib;
    private int score1;
    private int score2;
    private String address;

    public Student(int id, String pib, int score1, int score2, String address) {
        this.id = id;
        this.pib = pib;
        this.score1 = score1;
        this.score2 = score2;
        this.address = address;
    }

    public Student(String pib, int score1, int score2, String address) {
        this.pib = pib;
        this.score1 = score1;
        this.score2 = score2;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", pib='" + pib + '\'' +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", address='" + address + '\'' +
                '}';
    }
}
