package university;

import java.util.*;

class CrewMember {
    String name;
    int age;
    int bounty; // цена за поимку
    String dream;

    List<CrewMember> commands = new ArrayList<>();
    List<CrewMember> subordinates = new ArrayList<>();
    List<CrewMember> friends = new ArrayList<>();
    List<CrewMember> admires = new ArrayList<>(); // Обожать
    List<CrewMember> rivalries = new ArrayList<>(); // Соперничать

    public CrewMember(String name, int age, int bounty, String dream) {
        this.name = name;
        this.age = age;
        this.bounty = bounty;
        this.dream = dream;
    }

    public String getName() {
        return name; }

    public void addSubordinate(CrewMember subordinate) {
        subordinates.add(subordinate); }
    public void addCommand(CrewMember command) {
        commands.add(command); }
    public void addFriend(CrewMember friend) {
        friends.add(friend); }
    public void addAdmires(CrewMember admired) {
        admires.add(admired); }
    public void addRivalry(CrewMember rival) {
        rivalries.add(rival); }

    public void printRelations() {
        System.out.println(name + " является " + this.getClass().getSimpleName());
        System.out.println("Возраст: " + age);
        System.out.println("Награда за поимку: " + bounty);
        System.out.println("Мечта: " + dream);

        if (!commands.isEmpty()) {
            System.out.println(name + " командует:");
            for (CrewMember cm : commands) {
                System.out.println("  - " + cm.getName());
            }
        }

        if (!subordinates.isEmpty()) {
            System.out.println(name + " подчиняется:");
            for (CrewMember cm : subordinates) {
                System.out.println("  - " + cm.getName());
            }
        }

        if (!friends.isEmpty()) {
            System.out.println(name + " дружит с:");
            for (CrewMember cm : friends) {
                System.out.println("  - " + cm.getName());
            }
        }

        if (!admires.isEmpty()) {
            System.out.println(name + " обожает:");
            for (CrewMember cm : admires) {
                System.out.println("  - " + cm.getName());
            }
        }

        if (!rivalries.isEmpty()) {
            System.out.println(name + " соперничать с:");
            for (CrewMember cm : rivalries) {
                System.out.println("  - " + cm.getName());
            }
        }
    }
}

class Captain extends CrewMember {
    public Captain(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}
class Bosun extends CrewMember {
    public Bosun(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}
class Navigator extends CrewMember {
    public Navigator(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}
class Sailor extends CrewMember { // Матрос
    public Sailor(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}
class Cook extends CrewMember {
    public Cook(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}
class Carpenter extends CrewMember { // Плотник
    public Carpenter(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}
class Doctor extends CrewMember {
    public Doctor(String name, int age, int bounty, String dream) {
        super(name, age, bounty, dream); }
}