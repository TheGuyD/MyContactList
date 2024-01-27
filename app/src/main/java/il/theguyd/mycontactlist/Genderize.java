package il.theguyd.mycontactlist;

public class Genderize {
    private int count;
    private String name;
    private String gender;

    private float probability;

    public Genderize(int count, String name, String gender, float probability) {
        this.count = count;
        this.name = name;
        this.gender = gender;
        this.probability = probability;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
