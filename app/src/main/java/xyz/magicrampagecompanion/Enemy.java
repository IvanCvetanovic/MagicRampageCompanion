package xyz.magicrampagecompanion;

public class Enemy {
    private String name;
    private int health;
    private int damage;
    private int damageOnTouch;
    private int armor;
    private double speed;
    private double jump;
    private String patrolBehavour;
    private String attackBehaviour;
    private int imageResId;

    public Enemy(String name, int health, int damage, int damageOnTouch, int armor, double speed, double jump, String patrolBehavour, String attackBehaviour, int imageResId) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.damageOnTouch = damageOnTouch;
        this.armor = armor;
        this.speed = speed;
        this.jump = jump;
        this.patrolBehavour = patrolBehavour;
        this.attackBehaviour = attackBehaviour;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamageOnTouch() {
        return damageOnTouch;
    }

    public void setDamageOnTouch(int damageOnTouch) {
        this.damageOnTouch = damageOnTouch;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public String getPatrolBehavour() {
        return patrolBehavour;
    }

    public void setPatrolBehavour(String patrolBehavour) {
        this.patrolBehavour = patrolBehavour;
    }

    public String getAttackBehaviour() {
        return attackBehaviour;
    }

    public void setAttackBehaviour(String attackBehaviour) {
        this.attackBehaviour = attackBehaviour;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
