package Monsters;


public class MonsterEs extends Monster implements ElementalMonster{
    public MonsterEs(String nama, int level, int hp, int exp, elements elemen){
        super(nama, level, hp, exp, elemen);
    }
    
    @Override
    public void ElementalAttack(Monster monsterLawan){
       if(monsterLawan.getElemen() == Monster.elements.ANGIN){
            System.out.println("Serangan efektif!");
       }
       else{
            System.out.println("Serangan tidak efektif!");
       }
    }
    
}
