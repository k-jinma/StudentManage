package beans;

public class Student {

    private String id;
    private int age;
    private String address;


    public Student(String id, int age, String address) {
        this.id = id;
        this.age = age;
        this.address = address;
    }

    public void showInfo() {
        System.out.println("名前:" + this.id);
        System.out.println("年齢:" + this.age);
        System.out.println("住所:" + this.address);
    }


}
