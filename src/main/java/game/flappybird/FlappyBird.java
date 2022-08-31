package game.flappybird;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class FlappyBird implements Jogo {

    public Passaro passaro;
    public Random gerador = new Random();
    public int record = 0;
    public ScoreNumber scoreNumber;

    //[0->Start Screen] [1->Get Ready] [2->Game] [3->Game Over]
    public int game_state = 0;

    public double ground_offset = 0;
    public double background_offset = 0;

    //VELOCIDADE EIXO X DO CHÃO
    public double gvx = 70;

    //VELOCIDADE EIXO X DO FUNDO
    public double bvx = 70;

    public ArrayList<Cano> canos = new ArrayList<Cano>();
    
    public Timer timerCano;
    public Timer auxTimer;
    public Hitbox groundBox;

    private Acao addCano() {
        return new Acao() {
            public void executa() {
                canos.add(new Cano(getLargura(), gerador.nextInt(getAltura() - 112 - Cano.HOLESIZE)));
            }
        };
    }

    private Acao proxCena() {
        return new Acao() {
            public void executa() {
                game_state ++;
                game_state = game_state%4;
            }
        };
    }

    //ADICIONA OS ELEMENTOS NA TELA
    public FlappyBird(){
        passaro = new Passaro (35, (getAltura()-112)/2);

        timerCano = new Timer(2, true, addCano());
        
        addCano().executa();

        scoreNumber = new ScoreNumber(0);
        groundBox = new Hitbox(0, getAltura() - 112, getLargura(), getAltura());
    }

    @Override
    public String getTitulo() {
        return "Flappy Bird Game";
    }

    @Override
    public int getLargura() {
        return 384;
    }

    @Override
    public int getAltura() {
        return 512;
    }

    public void gameOver() {
        canos = new ArrayList<Cano>();
        passaro = new Passaro (35, (getAltura()-112)/2);
        proxCena().executa();
    }

    @Override
    public void tique(Set<String> teclas, double dt) {
        //MOVIMENTO GROUND
        ground_offset += dt * gvx;
        ground_offset = ground_offset % 308;

        //MOVIMENTO BACKGROUND
        background_offset += dt * bvx;
        background_offset = background_offset % 288;

        switch(game_state) {
        case 0: //MAIN SCREEN
                break;
        case 1: //GET READY
                auxTimer.tique(dt);
                passaro.updateSprite(dt);
                break;
        case 2: //GAME SCREEN
                timerCano.tique(dt);
                passaro.atualiza(dt);
                passaro.updateSprite(dt);

                if(groundBox.intersecao(passaro.box) != 0) {
                    gameOver();
                    return;
                }
                
                if(passaro.y < -5) {
                    gameOver();
                    return;
                }

                //MOVIMENTO CANO
                for(Cano cano : canos) {
                    cano.atualiza(dt);
                    if(cano.boxcima.intersecao(passaro.box) != 0 || cano.boxbaixo.intersecao(passaro.box) != 0) {
                        if(scoreNumber.getScore() > ScoreNumber.record) {
                            ScoreNumber.record = scoreNumber.getScore();
                        }
                        gameOver();
                        return;
                    }

                    if(!cano.counted && cano.x < passaro.x) {
                        cano.counted = true;
                        scoreNumber.modifyScore(1);
                    }
                }

                //REMOVENDO O CANO DEPOIS QUE PASSAR PARA A ESQUERDA DA TELA
                if(canos.size() > 0 && canos.get(0).x < -70) {
                    canos.remove(0);
                }
                break;
        case 3: //GAME OVER SCREEN
                break;
        }
    }

        /*
        timerCano.tique(dt);

        //MOVIMENTO PASSARO
        passaro.atualiza(dt);

        if(passaro.y + 24 >= getAltura() - 112) {
            System.out.println("GAME OVER NO CHÃO");
        } else if (passaro.y <= 0) {
            System.out.println("GAME OVER NO CEU");
            
        }

        //MOVIMENTO CANO
        for(Cano cano : canos) {
            cano.atualiza(dt);

            if(passaro.box.intersecao(cano.boxcima) != 0 || passaro.box.intersecao(cano.boxbaixo) != 0) {
                System.out.println("GAME OVER NO CANO");
            }
        }
         

        //REMOVENDO O CANO DEPOIS QUE PASSAR PARA A ESQUERDA DA TELA
        if(canos.size() > 0 && canos.get(0).x < - 60) {
            canos.remove(0);
        }


    }
    */

    @Override
    public void tecla(String tecla) {
        switch(game_state) {
        case 0:
                if(tecla.equals(" ")) {
                    auxTimer = new Timer(1.6, false, proxCena());
                    proxCena().executa();
                }
                break;
        case 1:
                break;
        case 2:
                if(tecla.equals(" ")) {
                    passaro.flap();
                }
                break;
        case 3:
                if(tecla.equals(" ")) {
                    scoreNumber.setScore(0);
                    proxCena().executa();
                }
                break;
        }
    }

    //DESENHA OS ELEMENTOS
    @Override
    public void desenhar(Tela tela) {
        //BACKGROUND
        tela.imagem("flappy.png", 0,0,288,512, 0, -background_offset, 0);
        tela.imagem("flappy.png", 0,0,288,512, 0, (288 -background_offset), 0);
        tela.imagem("flappy.png", 0,0,288,512, 0, ((288*2) -background_offset), 0);

        //CANO
        for(Cano cano : canos) {
            cano.desenha(tela);
        }

        //GROUND
        tela.imagem("flappy.png", 292, 0, 308, 112, 0, -ground_offset, getAltura() - 112);
        tela.imagem("flappy.png", 292, 0, 308, 112, 0, (308 -ground_offset), getAltura() - 112);
        tela.imagem("flappy.png", 292, 0, 308, 112, 0, ((308*2) -ground_offset), getAltura() - 112);
        
        switch(game_state){
            case 0:
                tela.imagem("flappy.png", 292, 346, 192, 44, 0, getLargura()/2 - 192/2, 100);
                tela.imagem("flappy.png", 352, 306, 70, 36, 0, getLargura()/2 - 70/2, 175);
                tela.texto("Pressione espaço", 60, getAltura()/2 - 16, 32, Cor.BRANCO);
                break;
            case 1:
                passaro.desenhar(tela);;
                tela.imagem("flappy.png",292,442,174,44, 0, getLargura()/2 - 174/2, getAltura()/3);
                scoreNumber.drawScore(tela, 5, 5);
                break;
            case 2:
                scoreNumber.drawScore(tela, 5, 5);
                passaro.desenhar(tela);
                break;
            case 3:
                tela.imagem("flappy.png", 292, 398, 188, 38, 0, getLargura()/2 - 188/2, 100);
                tela.imagem("flappy.png", 292, 116, 226, 116, 0, getLargura()/2 - 226/2, getAltura()/2 - 116/2);
                scoreNumber.drawScore(tela, getLargura()/2 + 50, getAltura()/2-25);
                scoreNumber.drawRecord(tela, getLargura()/2 + 55, getAltura()/2 + 16);
                break;
            }

    }

    public static void main(String[] args) {
        roda();
    }
    
    private static void roda() {
        new Motor(new FlappyBird());
    }
}
