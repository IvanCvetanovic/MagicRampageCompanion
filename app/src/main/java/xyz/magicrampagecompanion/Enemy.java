package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Enemy implements Parcelable {
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

    protected Enemy(Parcel in) {
        name = in.readString();
        health = in.readInt();
        damage = in.readInt();
        damageOnTouch = in.readInt();
        armor = in.readInt();
        speed = in.readDouble();
        jump = in.readDouble();
        patrolBehavour = in.readString();
        attackBehaviour = in.readString();
        imageResId = in.readInt();
    }

    public static final Creator<Enemy> CREATOR = new Creator<Enemy>() {
        @Override
        public Enemy createFromParcel(Parcel in) {
            return new Enemy(in);
        }

        @Override
        public Enemy[] newArray(int size) {
            return new Enemy[size];
        }
    };

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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getJump() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(health);
        dest.writeInt(damage);
        dest.writeInt(damageOnTouch);
        dest.writeInt(armor);
        dest.writeDouble(speed);
        dest.writeDouble(jump);
        dest.writeString(patrolBehavour);
        dest.writeString(attackBehaviour);
        dest.writeInt(imageResId);
    }
}
