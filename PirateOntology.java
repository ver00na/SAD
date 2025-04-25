package university;

import java.util.*;

public class PirateOntology {
    public static void main(String[] args) {
        Captain luffy = new Captain("Luffy", 17, 300000000, "Найти One Piece");
        Navigator nami = new Navigator("Nami", 18, 16000000, "Нарисовать карту всего мира");
        Sailor zoro = new Sailor("Zoro", 19, 120000000, "Стать сильнейшим фехтовальщиком");
        Cook sanji = new Cook("Sanji", 19, 77000000, "Найти All Blue");
        Bosun robin = new Bosun("Robin", 28, 80000000, "Найти Рио панеглиф");
        Doctor chopper = new Doctor("Chopper", 15, 50, "Создать панацею");
        Carpenter franky = new Carpenter("Franky", 29, 44000000, "Построить корабль, который пройдет Grand Line");
        Sailor usopp = new Sailor("Usopp", 17, 30000000, "Стать храбрым воином моря");
        Sailor brook = new Sailor("Brook", 88, 33000000, "Воссоединиться с Лабуном");

        Collections.addAll(luffy.commands, nami, zoro, sanji, robin, chopper, franky, usopp, brook);
        Collections.addAll(nami.commands, zoro, sanji, chopper, franky, usopp, brook);
        Collections.addAll(robin.commands, zoro, sanji, chopper, franky, usopp, brook);

        for (CrewMember cm : Arrays.asList(nami, zoro, sanji, robin, chopper, franky, usopp, brook)) {
            cm.addSubordinate(luffy);
        }

        for (CrewMember cm : Arrays.asList(zoro, sanji, chopper, franky, usopp, brook)) {
            cm.addSubordinate(nami);
            cm.addSubordinate(robin);
        }

        robin.addFriend(nami);
        nami.addFriend(robin);
        robin.addFriend(franky);
        franky.addFriend(robin);
        chopper.addFriend(usopp);
        usopp.addFriend(chopper);
        chopper.addFriend(luffy);
        luffy.addFriend(chopper);
        usopp.addFriend(luffy);
        luffy.addFriend(usopp);

        chopper.addAdmires(franky);
        sanji.addAdmires(nami);
        sanji.addAdmires(robin);

        zoro.addRivalry(sanji);
        sanji.addRivalry(zoro);
        brook.addRivalry(nami);
        nami.addRivalry(brook);
        luffy.addRivalry(sanji);
        sanji.addRivalry(luffy);

        Scanner scanner = new Scanner(System.in);
        Map<String, CrewMember> crewMap = new HashMap<>();
        crewMap.put("Luffy", luffy);
        crewMap.put("Nami", nami);
        crewMap.put("Sanji", sanji);
        crewMap.put("Robin", robin);
        crewMap.put("Zoro", zoro);
        crewMap.put("Chopper", chopper);
        crewMap.put("Franky", franky);
        crewMap.put("Usopp", usopp);
        crewMap.put("Brook", brook);

        while (true) {
            System.out.print("Введите имя члена команды: ");
            String query = scanner.nextLine();

            CrewMember member = crewMap.get(query);
            if (member != null) {
                member.printRelations();
            } else {
                System.out.println(query + " не найден.");
            }
        }
    }
}