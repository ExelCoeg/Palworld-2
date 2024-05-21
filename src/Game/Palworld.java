package Game;

import Monsters.*;
import java.util.*;

public class Palworld {
    /*
        GAME TURN BASE 
    * desc game:
    * - player bisa ke dungeon untuk cari monster liar untuk dilawan.
    * - player hanya bisa bawa 3 monster ke dungeon
    * - player bisa pilih 1 monster untuk bertarung
    * - monster milik player akan mendapatkan experience point (EP) setelah bertarung
    * - EP bisa digunakan untuk menaikkan level monster di homebase
    * - dungeon selalu generate monster liar secara acak
    * - saat bertemu monster liar, secara otomatis akan dibuat arena pertarungan
    * - player bisa menyerang, bertahan, atau lari dari pertarungan
    * 
     * Player capabilities:
     * 1. menyimpan semua monster miliknya 
     * 2. menaikkan level monsternya
     * 3. memulihkan health point (HP)
     * 4. monster setelah bertarung
     * 5. mengubah elemen monster (evo)
     *
     * Monster:
     * - nama, level, hp, exp, elemen
     * - level monster 1-99
     * - semakin besar level, semakin kuat serangannya
     * - kalau hp monster 0, monster pingsan -> bisa disadarkan di homebase
     * - exp didapat dari pertarungan
     * - exp bisa digunakan untuk naikin level monster dan beli item di homebase
     * - elemen monster: api, angin, air, es, tanah
     * - monster berevolusi -> elemen berubah, serangan elemen berubah
     * - evolusi hanya bisa dilakukan 1 kali dalam 1 level
     * - kalau mau evolusi lagi, monster harus naik level dulu
     * - perubahan elemen tidak bisa dilakukan ke sembarang elemen
     */    


    public static int PilihMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Selamat datang di Palworld!");
        System.out.println("1. Start Game");
        System.out.println("2. Load Game");
        System.out.println("3. Exit (Auto Save)");
        System.out.print("Pilih menu: ");

        int pilihMenu = sc.nextInt();
        
        sc.nextLine();
        return pilihMenu;
    }
   

    public static Map<String, Monster> ArraytoMap(List<Monster> monsters){
        Map<String, Monster> monsterMap = new HashMap<>();
        for(Monster monster : monsters){
            monsterMap.put(monster.getName(), monster);
        }
        return monsterMap;
    }
    public static void main(String[] args) {
        new UI();
    //     Scanner sc = new Scanner(System.in);
    //     Player player = new Player();
    //     int pilihMenu = PilihMenu();
    //     while(pilihMenu < 1 || pilihMenu > 3){
    //         System.out.println("Pilihan tidak valid!");
    //         pilihMenu = PilihMenu();
    //     }
    //     switch (pilihMenu) {
    //         case 1:
    //             System.out.println("Memulai game baru!");
    //             System.out.print("Masukkan nama player: ");
    //             String namaPlayer = sc.nextLine();
    //             player.setName(namaPlayer);
    //             System.out.println();
    //             System.out.println("Selamat datang di Palworld " + player.getName() + "!");
    //             System.out.println("Pal yang bisa dipilih sebagai Pal pertama:");
    //             System.out.println("1. Flameling (API)");
    //             System.out.println("2. Zephyrkin (ANGIN)");
    //             System.out.println("3. Aquabot (AIR)");
    //             System.out.println("4. Frostlet (ES)");
    //             System.out.println("5. Terrapup (TANAH)");
    //             System.out.print("Pilih Pal pertama: ");
    //             int pilihPal = sc.nextInt();
    //             System.out.println();
    //             player.monsters = new ArrayList<>();
                
    //             switch(pilihPal){
    //                 case 1 -> player.monsters.add(new MonsterApi("Flameling", 11, 100, 0, Monster.elements.API));
    //                 case 2 -> player.monsters.add(new MonsterAngin("Zephyrkin", 1, 100, 0, Monster.elements.ANGIN));
    //                 case 3 -> player.monsters.add(new MonsterAir("Aquabot", 1, 100, 0, Monster.elements.AIR));
    //                 case 4 -> player.monsters.add(new MonsterEs("Frostlet", 1, 100, 0, Monster.elements.ES));
    //                 case 5 -> player.monsters.add(new MonsterTanah("Terrapup", 1, 100, 0, Monster.elements.TANAH));
    //                 default -> System.out.println("Pilihan tidak valid!");
    //             }   System.out.println("Pal pertama yang dipilih: " + player.monsters.get(0).getName());
                
    //             GameState gameState = new GameState(namaPlayer, ArraytoMap(player.monsters), new ArrayList<>());
    //             // // Menyimpan GameState
    //             try {
    //                 SaveAndLoad.saveGame(gameState, gameState.getPlayerName());
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //             break;
    //         case 2:
    //             SaveAndLoad.savesList();
    //             System.out.print("Pilih save file yang ingin di load (nama file): ");
    //             String fileName = sc.nextLine();
    //             try {
    //                 gameState = SaveAndLoad.loadGame(fileName +".txt");
    //                 player.setName(gameState.getPlayerName());
    //                 System.out.println("Game berhasil di load!");
    //                 System.out.println("Player: " + player.getName());
    //                 player.monsters = new ArrayList<>(gameState.getMonsters().values());
    //             } catch (IOException | ClassNotFoundException e) {
    //                 System.out.println("Save file tidak ditemukan!");
    //                 System.exit(0);    
    //             }

    //             break;

    //         case 3:
    //             System.exit(0);
    //         default:
    //             System.out.println("Salah pilih menu!");
    //             break;
    //     }
    //     // game loop
    //     while(true){
    //         //-------------------------------HOME BASE--------------------------------
    //         Homebase homebase = new Homebase();
    //         GameState gameState = new GameState(player.getName(), ArraytoMap(player.monsters), new ArrayList<>());
    //         System.out.println("Jika anda ingin bertarung dengan monster pilih opsi '4. Keluar Homebase'");
    //         System.out.println("Selamat datang di Homebase !");
    //         System.out.println("Disini anda dapat melakukan: ");
    //         System.out.println("1. Heal Monster");
    //         System.out.println("2. Evolve Monster");
    //         System.out.println("3. LevelUp Monster");
    //         System.out.println("4. Beli Item");
    //         System.out.println("5. Lihat Monster");
    //         System.out.println("6. Keluar Homebase");
    //         System.out.print("Pilih opsi: ");
    //         int pilihMenuUtama = sc.nextInt();
    //         System.out.println();
    //         switch(pilihMenuUtama){
    //             case 1 -> {
    //                 System.out.println("Pilih monster anda yang ingin diheal!");
    //                 for(int i=0;i<player.monsters.size();i++){
    //                     System.out.println(i+1 + ". " + player.monsters.get(i).getName());
    //                 }
    //                 System.out.print("Pilih monster: ");
    //                 int monsterYangInginDiHealIndex = sc.nextInt();
    //                 homebase.HealMonster(player.monsters.get(monsterYangInginDiHealIndex-1));
    //                 System.out.println("Monster " + player.monsters.get(monsterYangInginDiHealIndex-1).getName() + " telah diheal!");
    //                 System.out.println();
    //                 System.out.println();
    //             }
    //             case 2->{
    //                 System.out.println("MONSTER HARUS LEVEL 10 UNTUK DAPAT DIEVOLVE");
    //                 System.out.println("Pilih monster anda yang ingin evolve!");
    //                 for(int i=0;i<player.monsters.size();i++){
    //                     Monster monster = player.monsters.get(i);
    //                     System.out.println(i+1 + ". " + monster.getName() + String.format(" (%s)",monster.getElemen()));
    //                 }
    //                 System.out.println("Cara menulis");
    //                 System.out.println("input:");
    //                 System.out.println("[index monster] [spasi] [elemen evolusi]");
    //                 int monsterYangInginDiEvolveIndex = sc.nextInt();
    //                 sc.nextLine();
    //                 String elemenEvolve = sc.nextLine();
    //                 Monster monsterEvolved = homebase.EvolveMonster(player.monsters.get(monsterYangInginDiEvolveIndex-1),elemenEvolve);
    //                 if(monsterEvolved == null){
    //                     System.out.println("Evolusi gagal!");
    //                 }
    //                 else{
    //                     player.monsters.set(monsterYangInginDiEvolveIndex-1, monsterEvolved);
    //                     System.out.println("Evolusi berhasil!");
    //                 }
    //                 System.out.println();
                    
    //             }
    //             case 3 ->{
    //                 System.out.println("Pilih monster yang ingin di level up!");
    //                 for(int i=0;i<player.monsters.size();i++){
    //                     System.out.println(i+1 + ". " + player.monsters.get(i).getName());
    //                 }
    //                 System.out.print("Pilih monster: ");
    //                 int monsterYangInginDiLevelUpIndex = sc.nextInt();
    //                 player.monsters.get(monsterYangInginDiLevelUpIndex-1).LevelUp();
    //                 System.out.println("Monster " + player.monsters.get(monsterYangInginDiLevelUpIndex-1).getName() + " telah di level up!");
    //                 System.out.println();
    //                 System.out.println();
    //             }
    //             case 5 ->{
    //                 player.PrintMonsters();
    //                 System.out.println();
    //             }
    //             case 6->{
    //                 boolean onHomebase = false;
    //                 while(!onHomebase){
    //                     System.out.println("Anda berada di luar homebase! (pilih opsi menu): ");
    //                     System.out.println("1. Dungeon");
    //                     System.out.println("2. Lihat monster");
    //                     System.out.println("3. Homebase");
    //                     System.out.println("4. Keluar dari Palworld");
    //                     System.out.print("Pilih opsi: ");
    //                     int pilihMenuLuar = sc.nextInt();
    //                     switch(pilihMenuLuar){
    //                         case 1 -> { 
    //                             //-------------------------------DUNGEON--------------------------------
    //                             Dungeon dungeon = new Dungeon();
            
    //                             System.out.println("Selamat datang di Dungeon!");
    //                             System.out.println("Silahkan pilih monster anda!: ");
            
    //                             for(int i=0;i<player.monsters.size();i++){
    //                                 System.out.println(i+1 + ". " + player.monsters.get(i).getName());
    //                             }
            
    //                             System.out.print("Pilih monster: ");
    //                             int pilihMonster = sc.nextInt();
    //                             Monster monsterDipilih = player.monsters.get(pilihMonster-1);
    //                             System.out.println("Monster yang dipilih: " + monsterDipilih.getName());
            
    //                             Monster monsterLawan = dungeon.GenerateRandomMonster(monsterDipilih);
                                
            
    //                             System.out.println("Monster yang ditemui: ");
    //                             monsterLawan.PrintInfo();
    //                             System.out.println("Pertarungan dimulai!");
    
    //                             // dungeon loop
    //                             while(true){
    //                                 System.out.println("Monster " + monsterDipilih.getName() + " vs " + monsterLawan.getName());
    //                                 System.out.println("Monster Anda:");
    //                                 monsterDipilih.PrintInfo();
    //                                 System.out.println("Monster Lawan:");
    //                                 monsterLawan.PrintInfo();
    //                                 System.out.println("1. Basic Attack");
    //                                 System.out.println("2. Special Attack");
    //                                 System.out.println("3. Elemental Attack");
    //                                 System.out.print("Pilih aksi: ");
    //                                 int pilihAksi = sc.nextInt();
    //                                 switch(pilihAksi){
    //                                     case 1 -> {
    //                                         monsterDipilih.BasicAttack(monsterLawan);
    //                                         System.out.println("Monster " + monsterDipilih.getName() + " menyerang " + monsterLawan.getName() + " dengan Basic Attack");
    //                                     }
    //                                     case 2 -> {
    //                                         monsterDipilih.SpecialAttack(monsterLawan);
    //                                         System.out.println("Monster " + monsterDipilih.getName() + " menyerang " + monsterLawan.getName() + " dengan Special Attack");
    //                                     }
    //                                     case 3 -> {
    //                                         if(monsterDipilih instanceof ElementalMonster elementalMonster){
    //                                             elementalMonster.ElementalAttack(monsterLawan);
    //                                             System.out.println("Monster " + monsterDipilih.getName() + " menyerang " + monsterLawan.getName() + " dengan Elemental Attack");
    //                                         }
    //                                         else
    //                                             System.out.println("Monster tidak memiliki serangan elemen");
    //                                     }
    //                                     default -> System.out.println("Pilihan tidak valid!");
    //                                 }
    //                                 System.out.println("Kemudian....");
    //                                 int aksiMonsterLawan = new Random().nextInt(2);
    //                                 switch(aksiMonsterLawan){
    //                                     case 0 -> {
    //                                         monsterLawan.BasicAttack(monsterDipilih);
    //                                         System.out.println("Monster " + monsterLawan.getName() + " menyerang " + monsterDipilih.getName() + " dengan Basic Attack");
    //                                     }
    //                                     case 1 -> {
    //                                         monsterLawan.SpecialAttack(monsterDipilih);
    //                                         System.out.println("Monster " + monsterLawan.getName() + " menyerang " + monsterDipilih.getName() + " dengan Special Attack");
    //                                     }
    //                                     case 2 ->{
    //                                         ((ElementalMonster) monsterLawan).ElementalAttack(monsterDipilih);
    //                                         System.out.println("Monster " + monsterLawan.getName() + " menyerang " + monsterDipilih.getName() + " dengan Elemental Attack");
    //                                     }
    //                                 }
    
                                    
    //                                 System.out.println();
    //                                 if(monsterDipilih.getHp() <= 0){
    //                                     System.out.println("Monster Anda pingsan!");
    //                                     break;
    //                                 }
    //                                 if(monsterLawan.getHp() <= 0){
    //                                     monsterDipilih.setExp(monsterLawan.getLevel() * 10);
    //                                     System.out.println("Monster lawan pingsan!");
    //                                     System.out.println("Anda mendapatkan " + monsterLawan.getLevel() * 10 + " exp!");
    //                                     break;
    //                                 }
    //                             }
    //                         }
    
    //                         case 2 -> {
    //                             //-------------------------------LIHAT MONSTER--------------------------------
    //                             player.PrintMonsters();
    //                             System.out.println();
    //                         }
    //                         case 3-> {
    //                             //-------------------------------HOME BASE--------------------------------
    //                             onHomebase = true;
    //                             break;
    //                         }
    //                         case 4 -> {
                                
    //                             System.out.println("Anda keluar dari Palworld!");
    //                         gameState = new GameState(player.getName(), ArraytoMap(player.monsters), new ArrayList<>());
                               
    //                             SaveAndLoad.overwriteGame(gameState, player.getName() + ".txt");
    //                             System.exit(0);
    //                         }
    //                     }
    //                 }
    //             }
    //         }
            
    //     }
    // }
    }
}
