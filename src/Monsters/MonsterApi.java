package Monsters;
public class MonsterApi extends Monster implements ElementalMonster {
    

    public MonsterApi(String nama, int level, int hp, int exp, elements api) {
        super(nama, level, hp, exp, api);
    }
    
    @Override
    public void ElementalAttack(Monster monsterLawan){
       if(monsterLawan.getElemen() == Monster.elements.ES){
            System.out.println("Serangan efektif!");
       }
       else{
            System.out.println("Serangan tidak efektif!");
       }
    }
   
}
