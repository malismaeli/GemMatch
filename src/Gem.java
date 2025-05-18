// Gem.java

/**
 * Gem Class
 * @author Mustafa Alismaeli
 * CS251 GemManager Part 1
 * Getters and Setters for printing
 */
public class Gem {
    private char type;

    public Gem(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }


    public String toString() {
        return String.valueOf(type);
    }
}