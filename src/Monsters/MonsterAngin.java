package Monsters;
public class MonsterAngin extends Monster implements ElementalMonster{
    public MonsterAngin(String nama, int level, int hp, int exp, elements elemen){
        super(nama, level, hp, exp, elemen);
    }
    
    @Override
    public void ElementalAttack(Monster monsterLawan){
       if(monsterLawan.getElemen() == Monster.elements.TANAH){
            System.out.println("Serangan efektif!");
       }
       else{
            System.out.println("Serangan tidak efektif!");
       }
    }
   
}
