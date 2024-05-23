package Monsters;

import Items.*;
import java.io.Serializable;
import java.util.Random;
enum elements{
    API,ES, ANGIN, TANAH, AIR;
}
public abstract class Monster implements Serializable{
    // public void BasicAttack(); // selalu kena musuh
    // public void SpecialAttack(); // lebih kuat, mengorbankan sekian persen HP sendiri, peluang rendah, dapat miss
    // public void UseItem(Potion potion); // gunakan potion untuk menambah HP atau elemental potion 
    // public void Escape(); // dapat escape tapi tidak selalu berhasil
    private String name;
    private int level = 1;
    private int hp = 100;
    private int exp = 0;
    public boolean evolved = false;
    private final int damage = 10;
    private elements elemen;
    public Monster(String name, int level, int hp, int exp, elements elemen){
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.exp = exp;
        this.elemen = elemen;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }


    public enum elements{
        API,ES, ANGIN, TANAH, AIR;
    }
    public elements getElemen() {
        return this.elemen;
    }
    public void LevelUp(){
        if(this.exp >= 100){
            this.level++;
        }
    }
    public void setElemen(elements elemen) {
        this.elemen = elemen;
    }
    public void setDamage(float damage){
    }
    public int getDamage(){
        return this.damage;
    }
    public void BasicAttack(Monster monster){
        monster.setHp(monster.getHp() - (int) getDamage());
    }
    public void SpecialAttack(Monster monster){
        Random rand = new Random();

        if(rand.nextInt(2) == 1){
            monster.setHp(getHp()- (int) getDamage()*2);
            this.setHp(hp - (int) getDamage()/2);
        }
        else{
            System.out.println("Special attack miss!");
        }
    } 
    public void UseItem(Potion potion){
        System.out.println("Monster menggunakan potion");
    }
    public void Escape(){
        System.out.println("Monster mencoba kabur");
    }
    public void Heal(){
        this.hp = 100;
    }
    public Monster Evolve(String elemen){
        Monster monsterEvolved = null;
        

        if(this.getLevel() >= 10 && !this.evolved){
            if(this.getElemen() == Monster.elements.API){
                if(elemen.equalsIgnoreCase("TANAH")){
                    monsterEvolved = new MonsterTanah(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterTanah.elements.TANAH);
                }
                else if(elemen.equalsIgnoreCase("ANGIN")){
                    monsterEvolved = new MonsterAngin(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterAngin.elements.ANGIN);
                }
            }

            if(this.getElemen() == Monster.elements.AIR){
                if(elemen.equalsIgnoreCase("ES")){
                    monsterEvolved = new MonsterEs(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterEs.elements.ES);
                }
                else if(elemen.equalsIgnoreCase("ANGIN")){
                    monsterEvolved = new MonsterAngin(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterAngin.elements.ANGIN);
                }
            }
            if(this.getElemen() == Monster.elements.TANAH){
                if(elemen.equalsIgnoreCase("API")){
                    monsterEvolved = new MonsterApi(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterApi.elements.API);
                }
                else if(elemen.equalsIgnoreCase("ES")){
                    monsterEvolved = new MonsterEs(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterEs.elements.ES);
                }
            }
            if(this.getElemen() == Monster.elements.ES){
                if(elemen.equalsIgnoreCase("AIR")){
                    monsterEvolved = new MonsterAir(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterAir.elements.AIR);
                }
                else if(elemen.equalsIgnoreCase("TANAH")){
                    monsterEvolved = new MonsterTanah(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterApi.elements.TANAH);
                }
            }
            if(this.getElemen() == Monster.elements.ANGIN){
                if(elemen.equalsIgnoreCase("API")){
                    monsterEvolved = new MonsterApi(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterApi.elements.API);
                }
                else if(elemen.equalsIgnoreCase("AIR")){
                    monsterEvolved = new MonsterAir(this.getName(), this.getLevel(), this.getHp(), this.getExp(), MonsterAir.elements.AIR);
                }
            }
            this.evolved = true;
        }
        return monsterEvolved;
    }
    public void PrintInfo(){
        System.out.println("name: " + getName());
        System.out.println("Level: " + getLevel());
        System.out.println("HP: " + getHp());
        System.out.println("Exp: " + getExp());
        System.out.println("Elemen: " + getElemen());
        System.out.println();
    }
}
