package game.flappybird;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class FlappyBird implements Jogo {

    public double ground_offset = 0;
    //VELOCIDADE EIXO X DO CHÃO
    public double gvx = 70;

    public double background_offset = 0;
    //VELOCIDADE EIXO X DO FUNDO
    public double bvx = 70;

    public Passaro passaro;
    public ArrayList<Cano> canos = new ArrayList<Cano>();
    public Random gerador = new Random();

    public Timer timerCano;

    //ADICIONA OS ELEMENTOS
    public FlappyBird(){
        passaro = new Passaro (35, (getAltura()-112)/2);

        timerCano = new Timer(3, true, addCano());
        
        addCano().executa();
    }

    private Acao addCano() {
        return new Acao() {
            public void executa() {
                canos.add(new Cano(getLargura() + 10, gerador.nextInt(getAltura() - 112 - Cano.HOLESIZE), -gvx));
            }
        };
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

    @Override
    public void tecla(String tecla) {
        if(tecla.equals(" ")) {
            passaro.flap();
        }
    }

    @Override
    public void tique(Set<String> teclas, double dt) {
        //MOVIMENTO GROUND
        ground_offset += dt*gvx;
        ground_offset = ground_offset%308;

        //MOVIMENTO BACKGROUND
        background_offset += dt*bvx;
        background_offset = background_offset%288;

        timerCano.tique(dt);

        //MOVIMENTO PASSARO
        passaro.atualiza(dt);

        //MOVIMENTO CANO
        for(Cano cano : canos) {
            cano.atualiza(dt);
        }

    }

    //DESENHA OS ELEMENTOS
    @Override
    public void desenhar(Tela tela) {
        //BACKGROUND
        tela.imagem("flappy.png", 0,0,288,512, 0, -background_offset, 0);
        tela.imagem("flappy.png", 0,0,288,512, 0, 288 -background_offset, 0);
        tela.imagem("flappy.png", 0,0,288,512, 0, 288*2 -background_offset, 0);

        //CANO
        for(Cano cano : canos) {
            cano.desenha(tela);
        }

        //GROUND
        tela.imagem("flappy.png", 292, 0, 308, 112, 0, -ground_offset, getAltura() - 112);
        tela.imagem("flappy.png", 292, 0, 308, 112, 0, 308 -ground_offset, getAltura() - 112);
        tela.imagem("flappy.png", 292, 0, 308, 112, 0, 308*2 -ground_offset, getAltura() - 112);
        
        //BIRD
        passaro.desenhar(tela);

    }

    public static void main(String[] args) {
        roda();
    }
    
    private static void roda() {
        new Motor(new FlappyBird());
    }
}
