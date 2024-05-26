package Monsters;


public class MonsterAir extends Monster implements ElementalMonster{
    public MonsterAir(String nama, int level, int hp, int exp, elements elemen){
        super(nama, level, hp, exp, elemen);
    }
    

    @Override
    public void ElementalAttack(Monster monsterLawan){
       if(monsterLawan.getElemen() == Monster.elements.API){
            monsterLawan.setHp(monsterLawan.getHp() - (int)(0.1 * monsterLawan.getHp()));
       }
       else{
            System.out.println("Serangan tidak efektif!");
       }
    }
}
